structure Type :> Type =
struct

  (* Position (line,column) *)
  type pos = int*int

  (* Use "raise Error (message,position)" for error messages *)
  exception Error of string*pos

  (* table of predefined conversion functions *)
  val functionTable : (string * (Fasto.Type list * Fasto.Type)) list ref
    = let val noPos = (0,0)
      in ref [ ("ord",([Fasto.Char noPos],Fasto.Int noPos)) (* ord: char->int *)
             , ("chr",([Fasto.Int noPos], Fasto.Char noPos)) (* chr: int->char *)
             , ("length", ([Fasto.Int noPos], Fasto.Int noPos)) (* length type is not expressible!*)
             ]
      end

  (* The module uses Fasto.Type, but its own printing functions which use
     positions, and extra operations to extract or update positions *)

  (* extracting positions *)
  fun typePos (Fasto.Int  pos) = SOME pos
    | typePos (Fasto.Bool pos) = SOME pos
    | typePos (Fasto.Char pos) = SOME pos
    | typePos (Fasto.Array (_, pos)) = SOME pos
    | typePos Fasto.UNKNOWN = NONE

  (* updating positions *)
  fun newPos pos (Fasto.Int  _) = Fasto.Int  pos
    | newPos pos (Fasto.Bool _) = Fasto.Bool pos
    | newPos pos (Fasto.Char _) = Fasto.Char pos
    | newPos pos (Fasto.Array (t, _)) = Fasto.Array (t,pos)
    | newPos pos Fasto.UNKNOWN = Fasto.UNKNOWN

  (* printing a type with location (for errors) *)
  fun showType t
    = let val loc = case typePos t of
                      SOME (l,c) => "at line " ^ Int.toString l
                                    ^ ", column " ^ Int.toString c
                    | NONE       => "inferred"
      in "Type " ^ Fasto.pp_type t ^ " (" ^ loc ^ ")"
      end

  (* specified in SML, but not implemented in Moscow ML *)
  fun concatWith inter ss
    = let fun intersperse []        = []
            | intersperse [s]       = [s]
            | intersperse (s::rest) = s :: inter :: intersperse rest
      in String.concat (intersperse ss)
      end

  (* pretty-printer for function types, for error messages *)
  fun showFunType ( [] ,res) = " () -> " ^ Fasto.pp_type res
    | showFunType (args,res) = concatWith " * " (List.map Fasto.pp_type args)
                               ^ " -> " ^ Fasto.pp_type res

  (* Type comparison, ignoring position *)
  fun typesEqual (Fasto.Int _, Fasto.Int _  ) = true
    | typesEqual (Fasto.Bool _,Fasto.Bool _ ) = true
    | typesEqual (Fasto.Char _, Fasto.Char _) = true
    | typesEqual (Fasto.Array (t1,_), Fasto.Array (t2,_)) = typesEqual (t1,t2)
    | typesEqual ( _ , _ ) = false


  (* flat partial ordering on types: UNKNOWN < all other types. First argument
     is position that led to the result (curried for use of foldl and map)
   *)
  fun unifyTypes pos (Fasto.UNKNOWN, t) = t
    | unifyTypes pos (t, Fasto.UNKNOWN) = t
    | unifyTypes pos (t1, t2) = let val exn = Error ("Cannot unify types "
                                                     ^ showType t1 ^ " and "
                                                     ^ showType t2, pos)
                                in if typesEqual (t1,t2) then newPos pos t1
                                                         else raise exn
                                end

  fun printable (Fasto.Int  pos) = true
    | printable (Fasto.Bool pos) = true
    | printable (Fasto.Char pos) = true
    | printable (Fasto.Array (Fasto.Char _, _)) = true
    | printable _ = false   (* true (*for polymorphic write*) *)

  (* Determine the type of an expression, decorate nodes with type on the way.
     An exception is raised immediately on the first inconsistency, in
     "unifyTypes" (could instead collect and report everything at the end). *)
  fun expType vs e
    = case e of
        Fasto.Num     (n,pos)     => (Fasto.Int pos , e)
      | Fasto.Log     (b,pos)     => (Fasto.Bool pos, e)
      | Fasto.CharLit (c,pos)     => (Fasto.Char pos, e)
      | Fasto.StringLit (s,pos)   => (Fasto.Array ((Fasto.Char pos), pos), e)
      | Fasto.ArrayLit ([],_,pos) => raise Error("Impossible empty array",pos)
      | Fasto.ArrayLit (e::es, t, pos)
        => let val (type_e,e_dec)    = expType vs e
               val (types_es,es_dec) = ListPair.unzip (List.map (expType vs) es)
               val all_types = List.foldl (unifyTypes pos) type_e types_es
            (* join will raise an exception if types do not match *)
           in (Fasto.Array (unifyTypes pos (t, all_types), pos),
               Fasto.ArrayLit (e_dec::es_dec, all_types, pos))
           end
      | Fasto.Var (s, pos)
        => (case SymTab.lookup s vs of
               NONE   => raise Error (("Unknown variable " ^ s ),pos)
             | SOME t => (t,e) (* NB: pos of declaration, not that of use *)
            )
      | Fasto.Plus (e1, e2, pos)
        => let val (ts, es)
                 = ListPair.unzip (List.map (expType vs) [e1,e2])
               val t  = List.foldl (unifyTypes pos) (Fasto.Int pos) ts
           in (Fasto.Int pos,
               Fasto.Plus (List.nth (es,0), List.nth (es,1),pos))
           end
      | Fasto.Minus (e1, e2, pos) (* same as Plus *)
        => let val (ts, es)
                 = ListPair.unzip (List.map (expType vs) [e1,e2])
               val t  = List.foldl (unifyTypes pos)(Fasto.Int pos) ts
           in (Fasto.Int pos,
               Fasto.Minus (List.nth (es,0), List.nth (es,1),pos))
           end

    (*******************************)
    (* Project solution: Task1 Beg *)
    (*******************************)

      | Fasto.Band (e1, e2, pos)
        => let val (ts, es)
                 = ListPair.unzip (List.map (expType vs) [e1,e2])
               val t  = List.foldl (unifyTypes pos)(Fasto.Int pos) ts
           in (Fasto.Int pos,
               Fasto.Band (List.nth (es,0), List.nth (es,1), pos))
           end
      | Fasto.Times (e1, e2, pos)
        => let val (ts, es)
                 = ListPair.unzip (List.map (expType vs) [e1,e2])
               val t  = List.foldl (unifyTypes pos)(Fasto.Int pos) ts
           in (Fasto.Int pos,
               Fasto.Times (List.nth (es,0), List.nth (es,1), pos))
           end
      | Fasto.Divide (e1, e2, pos)
        => let val (ts, es)
                 = ListPair.unzip (List.map (expType vs) [e1,e2])
               val t  = List.foldl (unifyTypes pos)(Fasto.Int pos) ts
           in (Fasto.Int pos,
               Fasto.Divide (List.nth (es,0), List.nth (es,1), pos))
           end
      | Fasto.And (e1, e2, pos)
        => let val (ts, es)
                 = ListPair.unzip (List.map (expType vs) [e1,e2])
               val t  = List.foldl (unifyTypes pos)(Fasto.Bool pos) ts
           in (Fasto.Bool pos,
               Fasto.And (List.nth (es,0), List.nth (es,1), pos))
           end
      | Fasto.Or (e1, e2, pos)
        => let val (ts, es)
                 = ListPair.unzip (List.map (expType vs) [e1,e2])
               val t  = List.foldl (unifyTypes pos)(Fasto.Bool pos) ts
           in (Fasto.Bool pos,
               Fasto.Or (List.nth (es,0), List.nth (es,1), pos))
           end
      | Fasto.Not (e', pos)
        => let val (t, eDec) = expType vs e'
           in if typesEqual (Fasto.Bool pos, t)
              then (Fasto.Bool pos, Fasto.Not (eDec, pos))
              else raise Error ("Boolean expected", pos)
           end
      | Fasto.Negate (e', pos)
        => let val (t, eDec) = expType vs e'
           in if typesEqual (Fasto.Int pos, t)
              then (Fasto.Int pos, Fasto.Negate (eDec, pos))
              else raise Error ("Integer expected", pos)
           end

    (*******************************)
    (* Project solution: Task1 End *)
    (*******************************)



      | Fasto.Equal (e1, e2, pos) (* comparable arg types, result bool *)
        => let val (ts, es)= ListPair.unzip (List.map (expType vs) [e1,e2])
               val el_type = List.foldl (unifyTypes pos) Fasto.UNKNOWN ts
           in case el_type of
                  Fasto.UNKNOWN => raise Error ("Cannot infer comparison type"
                                              , pos)
                | Fasto.Array _ => raise Error ("Cannot compare arrays", pos)
                | _             => (Fasto.Bool pos,
                                    Fasto.Equal (List.nth (es,0),
                                                 List.nth (es,1), pos))
           end
      | Fasto.Less (e1, e2, pos) (* same as Equal  TODO add type!! *)
        => let val (ts, es)= ListPair.unzip (List.map (expType vs) [e1,e2])
               val el_type = List.foldl (unifyTypes pos) Fasto.UNKNOWN ts
           in case el_type of
                  Fasto.UNKNOWN => raise Error ("Cannot infer comparison type"
                                              , pos)
                | Fasto.Array _ => raise Error ("Cannot compare arrays", pos)
                | _             => (Fasto.Bool pos,
                                    Fasto.Less (List.nth (es,0),
                                                List.nth (es,1), pos))
           end
      | Fasto.If (pred,e1,e2,pos)
        => let val (ts, es)
                 = ListPair.unzip (List.map (expType vs) [pred,e1,e2])
               val pred_type = List.nth (ts,0)
               val target_type
                 = List.foldl (unifyTypes pos) Fasto.UNKNOWN (List.tl ts)
           in if typesEqual (pred_type, Fasto.Bool pos)
                             then (target_type,
                                   Fasto.If (List.nth (es,0), List.nth (es,1),
                                             List.nth (es,2), pos))
                             else raise Error ("Non-boolean predicate", pos)
           end

      | Fasto.Apply ("length", args, pos) =>
            let val (arg_types, arg_decs) =
                                            ListPair.unzip (List.map (expType vs) args)
                val () = if(length arg_types = 1) then ()
                         else raise Error("Length receives exactly one argument!", pos)
                val () = case (hd arg_types) of
                           Fasto.Array(tp, pp) => ()
                         | otherwise => raise Error("Length argument not an array!", pos)
            in ( Fasto.Int(pos), Fasto.Apply("length", arg_decs, pos) )
            end
      | Fasto.Apply (f, args, pos)
        => let val (expected_args, res_type)
                 = case SymTab.lookup f (!functionTable) of
                       SOME x => x
                     | NONE   => raise Error ("Unknown function " ^ f, pos)
               val (arg_types, args_dec)
                 = ListPair.unzip (List.map (expType vs) args)
               fun checkargs   []    [] = ()
                 | checkargs   []    _
                      = raise Error ("Surplus argument in function call", pos)
                 | checkargs (t::_)  []
                      = raise Error ("Missing argument ( type "
                                     ^ Fasto.pp_type t
                                     ^") in function call", pos)
                 | checkargs (t::ts) (u::us)
                      = (unifyTypes pos (t,u); checkargs ts us)
           in checkargs expected_args arg_types;
              (res_type, Fasto.Apply (f,args_dec, pos))
           end
      | Fasto.Let (Fasto.Dec (name, exp1, pos1), exp2, pos)
        => let val (t1, exp1dec) = expType vs exp1
	       val (t2, e2dec) = expType (SymTab.bind name t1 vs) exp2
	   in (t2, Fasto.Let (Fasto.Dec (name, exp1dec, pos1), e2dec, pos))
           end

      | Fasto.Read  (t, pos) =>
        let val () = case t of
                       Fasto.Bool(pp) => ()
                     | Fasto.Int (pp) => ()
                     | Fasto.Char(pp) => ()
                     | otherwise => raise Error("Type argument of read needs to be a basic type!",pos)
        in (t, e)
        end

      | Fasto.Write (exp, t, pos)
        => let val (expType, exp_dec) = expType vs exp
           in (expType, Fasto.Write (exp_dec, expType, pos))
           end

      | Fasto.Index (s, exp, t, pos)
        => let val (e_type, expdec) = expType vs exp
               val a_type = case SymTab.lookup s vs of
                                NONE
                                => raise Error ("Unknown identifier " ^ s, pos)
                              | SOME (Fasto.Array (t,_)) => t
                              | SOME other
                                => raise Error (s ^ " has type " ^ showType other
                                                ^ ": not an array", pos)
           in (a_type, Fasto.Index (s,expdec, a_type, pos))
           end
      | Fasto.Iota (exp, pos)
        => let val (e_type, exp_dec) = expType vs exp
           in if typesEqual (e_type, Fasto.Int pos)
              then (Fasto.Array (Fasto.Int pos, pos), Fasto.Iota (exp_dec,pos))
              else raise Error ("Iota: wrong argument type "^ showType e_type,
                                pos)
           end

      | Fasto.Map ("length", arr, arg_t, res_t, pos) =>
            let val (arr_type, arr_dec) = expType vs arr
                val el_type = case arr_type of
                                Fasto.Array(Fasto.Array(tt, pp1), pp2) => Fasto.Array(tt, pos)
                              | otherwise => raise Error("In Map(length, arr), arr should be at least"^
                                                         " a two-dimensional array!", pos)
            in ( Fasto.Array( Fasto.Int(pos), pos ),
                 Fasto.Map( "length", arr_dec, el_type, Fasto.Int(pos), pos )
               )
            end
      | Fasto.Map (f, arr, arg_t, res_t, pos)
        => let val (arr_type, arr_dec) = expType vs arr
               val el_type
                 = case arr_type of
                      Fasto.Array (t,_) => unifyTypes pos (arg_t, t)
                    | other => raise Error ("Map: Argument not an array",pos)
               val (f_arg_type, f_res_type)
                 = case SymTab.lookup f (!functionTable)of
                       NONE => raise Error ("Unknown identifier " ^ f, pos)
                     | SOME ([a1],res) => (a1,res)
                     | SOME (args,r)
                       => raise Error ("Map: incompatible function type of "
                                       ^ f ^ ":" ^ showFunType (args,r), pos)
           in if typesEqual (el_type, f_arg_type)
              then (Fasto.Array (unifyTypes pos (res_t, f_res_type),pos),
                    Fasto.Map (f, arr_dec, el_type, f_res_type, pos))
              else raise Error ("Map: array element types does not match."
                                ^ Fasto.pp_type el_type ^ " instead of "
                                ^ Fasto.pp_type f_arg_type , pos)
           end

      | Fasto.Split (num, arr, typ, pos)
        => let
            (* type of first argument, should be Int *)
            val (n_type, n_dec) = expType vs num
            (* type of second argument, should be Array *)
            val (arr_type, arr_dec) = expType vs arr
            (* type of elements in Array, if not Array and exception is thrown *)
            val el_type = case arr_type of
                               Fasto.Array (t,_) => unifyTypes pos (typ, t)
                             | _ => raise Error ("Split: Second argument not an array",pos)
           in
             (* check if first argument is Int *)
             if typesEqual (n_type, Fasto.Int pos)
             then (Fasto.Array (arr_type, pos), Fasto.Split (num ,arr_dec, el_type, pos))
             else raise Error ("Split: First argument type is not Int " ^ showType n_type, pos)
           end

      | Fasto.Concat (arr1, arr2, arg1_typ, pos)
        => let
            (* type of first argument, should be Array *)
            val (arr1_type, arr1_dec) = expType vs arr1
            (* type of second argument, should be Array *)
            val (arr2_type, arr2_dec) = expType vs arr2
            (* type of elements in Array, if not Array and exception is thrown *)

            val el1_type
              = case arr1_type of
                  Fasto.Array (t,_) => unifyTypes pos (arg1_typ, t)
                | other => raise Error ("Concat: First Array Argument not an array",pos)

            val el2_type
              = case arr2_type of
                  Fasto.Array (t,_) => unifyTypes pos (arg1_typ, t)
                | other => raise Error ("Concat: Second Array Argument not an array",pos)

           in
             (* check if first argument is Int *)
             if typesEqual (el1_type, el2_type)
             then (Fasto.Array (el1_type, pos), Fasto.Concat
             (arr1_dec, arr2_dec, el1_type, pos))
             else raise Error ("Concat: Types in Array must be the same " ^
             showType el1_type, pos)
           end

      | Fasto.ZipWith (f, arr1, arr2, arg_t1, arg_t2, res_t, pos)
        => let val (arr1_type, arr1_dec) = expType vs arr1
               val (arr2_type, arr2_dec) = expType vs arr2

               val el1_type
                 = case arr1_type of
                      Fasto.Array (t,_) => unifyTypes pos (arg_t1, t)
                    | other => raise Error ("ZipWith: First Array Argument not an array",pos)

               val el2_type
                 = case arr2_type of
                      Fasto.Array (t,_) => unifyTypes pos (arg_t2, t)
                    | other => raise Error ("ZipWith: Second Array Argument not an array",pos)

               val (f_arg1_type, f_arg2_type, f_res_type)
                 = case SymTab.lookup f (!functionTable)of
                       NONE => raise Error ("Unknown identifier " ^ f, pos)
                     | SOME ([a1,a2],res) => (a1,a2,res)
                     | SOME (args,r)
                       => raise Error ("ZipWith: incompatible function type of "
                                       ^ f ^ ":" ^ showFunType (args,r), pos)
           in if ( typesEqual(el1_type, f_arg1_type) andalso typesEqual(el2_type, f_arg2_type) )
              then (Fasto.Array (unifyTypes pos (res_t, f_res_type),pos),
                    Fasto.ZipWith(f, arr1_dec, arr2_dec, el1_type, el2_type, f_res_type, pos))
              else raise Error ("ZipWith: arrays element types does not match."
                                ^ Fasto.pp_type el1_type ^ " instead of "
                                ^ Fasto.pp_type f_arg1_type ^ " or " ^ Fasto.pp_type el2_type ^
                                " instead of "^ Fasto.pp_type f_arg1_type  , pos)
           end

      | Fasto.Reduce (f, n, arr, t, pos)
        => let val (n_type, n_dec) = expType vs n
               val (arr_type, arr_dec) = expType vs arr
               val el_type
                 = case arr_type of
                      Fasto.Array (t,_) => t
                    | other => raise Error ("Reduce: Argument not an array",pos)
               val f_arg_type
                 = case SymTab.lookup f (!functionTable) of
                       NONE => raise Error ("Unknown identifier " ^ f, pos)
                     | SOME ([a1,a2],res)
                       => if typesEqual (a1,a2) andalso typesEqual (a2,res)
                          then res
                          else raise Error
                                  ("Reduce: incompatible function type of "
                                   ^ f ^": " ^ showFunType ([a1,a2],res),pos)
                     | SOME (args,r)
                       => raise Error ("Reduce: incompatible function type of "
                                       ^ f ^ ": " ^ showFunType (args,r),pos)
               fun err (s,t) = Error ("Reduce: unexpected " ^ s ^ " type "
                                      ^ Fasto.pp_type t ^ ", expected "
                                      ^ Fasto.pp_type f_arg_type, pos)
           in if typesEqual (el_type, f_arg_type)
              then if typesEqual (el_type, n_type)
                   then (unifyTypes pos (t, el_type),
                         Fasto.Reduce (f,n_dec, arr_dec, el_type, pos))
                   else raise (err ("neutral element", n_type))
              else raise err ("array element", el_type)
           end

      | Fasto.Scan (f, n, arr, t, pos)
        => let val (n_type, n_dec) = expType vs n
               val (arr_type, arr_dec) = expType vs arr
               val el_type
                 = case arr_type of
                      Fasto.Array (t,_) => t
                    | other => raise Error ("Scan: Argument not an array",pos)
               val f_arg_type
                 = case SymTab.lookup f (!functionTable) of
                       NONE => raise Error ("Unknown identifier " ^ f, pos)
                     | SOME ([a1,a2],res)
                       => if typesEqual (a1,a2) andalso typesEqual (a2,res)
                          then res
                          else raise Error
                                  ("Scan: incompatible function type of "
                                   ^ f ^": " ^ showFunType ([a1,a2],res),pos)
                     | SOME (args,r)
                       => raise Error ("Scan: incompatible function type of "
                                       ^ f ^ ": " ^ showFunType (args,r),pos)
               fun err (s,t) = Error ("Scan: unexpected " ^ s ^ " type "
                                      ^ Fasto.pp_type t ^ ", expected "
                                      ^ Fasto.pp_type f_arg_type, pos)
           in if typesEqual (el_type, f_arg_type)
              then if typesEqual (el_type, n_type)
                   then ( Fasto.Array(unifyTypes pos (t, el_type), pos ),
                         Fasto.Scan (f,n_dec, arr_dec, el_type, pos))
                   else raise (err ("neutral element", n_type))
              else raise err ("array element", el_type)
           end



      | Fasto.Replicate (n, exp, t, pos)
        => let val (n_type, n_dec) = expType vs n
               val (expType, exp_dec) = expType vs exp
           in if typesEqual (n_type, Fasto.Int pos)
              then (Fasto.Array (expType, pos),
                    Fasto.Replicate (n_dec,exp_dec, expType, pos))
              else raise Error ("Replicate: wrong argument type "
                                ^ showType n_type, pos)
           end




  (* Functions are guaranteed by syntax to have a known declared type.  This
     type is checked against the type of the function body, taking into
     account declared argument types and types of other functions called.

   *)
  fun checkAndDecorate (fname, ret_tp, args, body, pos)
      = let val (body_type, decorated) = expType args body
        in (fname, unifyTypes pos (body_type, ret_tp), args, decorated, pos)
        end

  (* function type table contains (fname, ([arg types],result type)) entries
   We do not have currying, so real "arrows" are not necessary *)
  fun getType  (fname, ret_tp, args, _, _)
    = (fname, (List.map (fn (_,t) => t) args, ret_tp))

  fun checkProgram funDecs =
   let val pos_table = List.map (fn (f,_,_,_,pos) => (f,pos)) funDecs
   in
    let val () = functionTable := List.foldr
                                    (fn ((n,ts),tab) => SymTab.insert n ts tab)
                                    (!functionTable) (map getType funDecs)
        (* function table was passed around everywhere, could be removed *)
        val decorated = List.map checkAndDecorate funDecs
        val main_pos  = case SymTab.lookup "main" pos_table of
                            NONE
                            => raise Error ("No main function defined.", (0,0))
                          | SOME pos => pos
    in
        (* check main function presence and type () -> int *)
        case SymTab.lookup "main" (!functionTable) of
            NONE => raise Error ("impossible case!", main_pos)
          | SOME ([],_)
            => decorated (* all fine, return *)
          | SOME (args,res)
            => raise Error ("Unexpected argument to main: "
                            ^ showFunType (args,res)
                            ^ " (should be () -> <anything>).", main_pos)
    end
    handle SymTab.Duplicate key
           => let val pos = case SymTab.lookup key pos_table of
                                NONE   => (0,0)
                              | SOME p => p
              in raise Error ("duplicate function " ^ key, pos)
              end
   end

end
