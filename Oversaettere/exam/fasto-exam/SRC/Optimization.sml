structure Optimization  = struct
    open Fasto

  (* Use "raise Error (message,position)" for error messages *)
  exception Error of string*(int*int)
  exception NotInSymTab of string

  (* Name generator.  Call with, e.g., t1 = "tmp"^newName () *)
  val counter = ref 0

  fun newName () = (counter := !counter + 1;
                  "_" ^ Int.toString (!counter)^ "_")

  fun zip (x::xs) (y::ys) = (x,y) :: (zip xs ys)
    | zip _       _       = [];

  (******************************************************************)
  (*** Adding/removing special funs "ord" and "chr" from FunSymTab***)
  (******************************************************************)
  fun add_spec_funs(funlst : FunDec list) : FunDec list =
      let val noPos = (0,0)
          val specfuns = [("ord", Fasto.Int  noPos,  (* ord: char->int *)
                                 [("ft_prm_"^newName(), Fasto.Char noPos)],
                                 Num(1,noPos), noPos)
                         ,("chr", Fasto.Char noPos,  (* chr: int->char *)
                                 [("ft_prm_"^newName(), Fasto.Int  noPos)],
                                 CharLit(#"a",noPos), noPos)
                         ,("length", Fasto.Int noPos,
                                 [("ft_prm_"^newName(), Fasto.Array(Fasto.Int(noPos), noPos))],
                                 Num(1,noPos), noPos )
                         ]
      in specfuns@funlst
      end

  fun rem_spec_funs(funlst : FunDec list) : FunDec list =
        List.filter
            (fn (fid, rtp, fargs, body, pdecl) =>
                (fid<>"ord" andalso fid<>"chr" andalso fid <>"length")  )
            funlst


  (******************************************************************)
  (*** Renaming Internal Variables of a Function with Unique Names***)
  (******************************************************************)
  fun unique_rename( exp : Exp, vtab ) : Exp = case exp of
         Let( Dec(id,e1,p1), e2, p2 ) =>
             let val new_id   = id^"_ft_exp_"^newName()
                 val new_vtab = SymTab.bind id new_id vtab
             in Let( Dec(new_id, unique_rename(e1, new_vtab), p1 ),
                                 unique_rename(e2, new_vtab), p2    )
             end
       | Band (e1, e2, p) =>  Band ( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )
       | Plus (e1, e2, p) =>  Plus ( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )
       | Minus(e1, e2, p) =>  Minus( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )
       | Equal(e1, e2, p) =>  Equal( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )
       | Less (e1, e2, p) =>  Less ( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )

       | Times (e1,e2,p) => Times ( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )
       | Divide(e1,e2,p) => Divide( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )
       | And   (e1,e2,p) => And   ( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )
       | Or    (e1,e2,p) => Or    ( unique_rename(e1, vtab), unique_rename(e2, vtab),  p )
       | Not   (e,p)     => Not   ( unique_rename(e , vtab), p )
       | Negate(e,p)     => Negate( unique_rename(e , vtab), p )

       | If(e1, e2, e3, p)=>  If   ( unique_rename(e1, vtab),
                                     unique_rename(e2, vtab),
                                     unique_rename(e3, vtab), p )
       | Apply(id,args,p) => Apply ( id, map (fn x => unique_rename(x, vtab)) args,  p )
       | Map(id,e,t1,t2,p)=> Map   ( id, unique_rename(e, vtab), t1, t2, p )
       | Reduce(id,e,l,t,p)=>Reduce( id, unique_rename(e, vtab), unique_rename(l, vtab), t, p )
       | Scan  (id,e,l,t,p)=> Scan ( id, unique_rename(e, vtab), unique_rename(l, vtab), t, p )

       | ZipWith(id, e1, e2, t1, t2, t3, p) => ZipWith(id, unique_rename(e1, vtab), unique_rename(e2, vtab), t1, t2, t3, p )

       | Iota (e, p)      => Iota  ( unique_rename(e, vtab), p )
       | Replicate(e,n,t,p)=>Replicate( unique_rename(e, vtab), unique_rename(n, vtab), t, p )
       | Concat(e1,e2,t,p)  => Concat( unique_rename(e1, vtab), unique_rename(e2, vtab), t, p )
       | Split(e1,e2,t,p)  => Split( unique_rename(e1, vtab), unique_rename(e2, vtab), t, p )
       | Write (e,t,p)    => Write ( unique_rename(e, vtab), t, p )
       | Index(id,e,t,p)  =>
            let val new_id   = case SymTab.lookup id vtab of NONE => id | SOME m => m
            in  Index( new_id, unique_rename(e, vtab), t, p )
            end
       | Var(id,p) => let val new_id   = case SymTab.lookup id vtab of NONE => id | SOME m => m
                      in  Var( new_id, p ) end

       | ArrayLit (lst,tp,pos) => ArrayLit ( map (fn x => unique_rename(x, vtab)) lst,  tp, pos )

       | Read     (t,p) => exp
       | Num      (n,p) => exp
       | Log      (b,p) => exp
       | CharLit  (c,p) => exp
       | StringLit(s,p) => exp

       | _ => raise Error("Unique_Rename Unimplemented AbSyn node "^pp_exp 0 exp, (0,0))
(*       | _ => exp   *)

  (*******************************************************************)
  (*** Dead-Function Elimination: Implement function ``live_funs''!***)
  (***   (delete the current implementation which identifies       ***)
  (***    all functions as live)                                   ***)
  (*******************************************************************)



  (***************************************************************)
  (*** dead-function elimination                               ***)
  (***************************************************************)

  (*** Two helper funs: one returns the name of the function  ***)
  (***                    called in Apply, Map, Reduce constr,***)
  (***                  the other returns for an expression   ***)
  (***                    its expression childreen            ***)

  fun getFunId  (fid ,_,_,_,_) = fid
  fun getFunBody(_,_,_,body,_) = body

  fun getUsedFun(exp : Fasto.Exp) : string option =
      case exp of
        Apply(fid, args, p)       => SOME fid
      | Map(fid, e, t1, t2, p)    => SOME fid
      | Reduce(fid, e1, e2, t, p) => SOME fid
      | Scan  (fid, e1, e2, t, p) => SOME fid
      | ZipWith(fid,_,_,_,_,_, p) => SOME fid
      | _                         => NONE

  fun getInnerExps(exp : Fasto.Exp) : Fasto.Exp list = case exp of
    (* constructors with no exp arguments *)
      Num(i,p)       => []
    | Log(b,p)       => []
    | CharLit(c,p)   => []
    | StringLit(s,p) => []
    | Var(id, p)     => []
    | Read(t, p)     => []

    (* constructors with ONE exp arguments *)
    | Not   (e, p)              => [e]
    | Negate(e, p)              => [e]
    | Iota  (e, p)              => [e]
    | Index (id, e, t, p)       => [e]
    | Write (e, t, p)           => [e]
    | Map   (fid, e, t1, t2, p) => [e]

    (* constructors with TWO exp arguments *)
    | Let( Dec(id,e1,p1), e2, p2 ) => [e1, e2]
    | Plus (e1, e2, p)             => [e1, e2]
    | Band (e1, e2, p)             => [e1, e2]
    | Minus(e1, e2, p)             => [e1, e2]
    | Equal(e1, e2, p)             => [e1, e2]
    | Less (e1, e2, p)             => [e1, e2]

    | Times(e1, e2, p)             => [e1, e2]
    | Divide(e1, e2, p)            => [e1, e2]
    | And(e1, e2, p)               => [e1, e2]
    | Or(e1, e2, p)                => [e1, e2]

    | Replicate(e1, e2, t, p)      => [e1, e2]
    | Concat(e1, e2, t, p)      => [e1, e2]
    | Split(e1, e2, t, p)      => [e1, e2]
    | Reduce(fid, e1, e2, t, p)    => [e1, e2]

    | Scan   (fid, e1, e2, t, p)       => [e1, e2]
    | ZipWith(fid, e1, e2, t, q, u, p) => [e1, e2]


    (* constructors with THREE exp arguments *)
    | If(e1, e2, e3, p) => [e1, e2, e3]

    (* constructors with A LIST of exp arguments *)
    | ArrayLit(lst, tp, p)    => lst
    | Apply(fid, args, p)     => args

    | _ => raise Error("getInnerExps: Unimplemented AbSyn node", (0,0))

  (*****************************************************)
  (*** live_fun: returns a list of unique function   ***)
  (***             names referenced in an expression ***)
  (*****************************************************)
  fun live_funs (
            exp    : Fasto.Exp,
            livefs : string list,
            ftab   : (string * Fasto.FunDec) list
      ) : string list =
    let val argexps  = getInnerExps(exp)
        val fidopt   = getUsedFun  (exp)
        val newlives = foldl (fn(e,lives)=>live_funs(e, lives, ftab)) livefs argexps
    in case fidopt of
         NONE     => newlives
       | SOME fid =>
           if( List.exists (fn id => id = fid) newlives )
           then newlives
           else case (SymTab.lookup fid ftab) of
                  NONE   => raise Error("Function "^fid^" not found in SymTab", (0,0))
                | SOME f => live_funs( getFunBody(f), fid::newlives, ftab )
    end



  fun dead_fun_elim ( funs : Fasto.FunDec list ) : FunDec list =
    let val ftab = map (fn f => (getFunId(f), f)) funs
    in  case (SymTab.lookup "main" ftab) of
          NONE => raise Error("Function main not found in SymTab", (0,0))
        | SOME mainf =>
            let val livefuns = live_funs(getFunBody(mainf), ["main"], ftab)
                fun getfun id = case (SymTab.lookup id ftab) of
                                  SOME f => f
                                | NONE   => raise NotInSymTab(id)
            in  (map getfun livefuns)
            end
    end

  (***************************************************************)
  (*** Map Fusion:   Two Phase Parse:                          ***)
  (*** 1. We propagate the map-fusion information top-down     ***)
  (***    a. Variables are assumed to have unique names in the ***)
  (***       function body->the renaming phase preceeds fusion ***)
  (***    b. Each Fasto.Let node associated with a map inherits***)
  (***       parrent-map information via symbol table "vtab"   ***)
  (***       and merges it with its own info. It also          ***)
  (***       initializes its reference counter and increases   ***)
  (***     the reference counter of its "parent" map node whith***)
  (***       whom it might fuse later.  If in the return pass  ***)
  (***       the current map node has reference count 1, i.e., ***)
  (***       only one map node uses it,then it sets its counter***)
  (***       to -1, signaling to its child that map fusion     ***)
  (***       is possible!                                      ***)
  (*** 2. see the "map_fusion_gen" function                    ***)
  (***************************************************************)


  (***            LET node    RefCount RefCountMap  var_id   funs to comp   ***)
  type SymTabEl = Fasto.Exp * int ref * int ref * ( string * string list ) list

  (** returns the persistent symbol table of the expression **)
  fun map_fusion_gather( exp : Exp, (vtab, ok) )   =
    case exp of
      Let( Dec(id,e1,p1), e2, p2 ) =>
          let val count    = ref 0
              val countmap = ref 0
              val mapinf   = case e1 of
                  Map(fid, Var(arr_id,p), t1, t2, pos) =>
                      let (** propagate the potential-fusion information from parent **)
                          val (e3,ct,ctm,mapparinf) = case (SymTab.lookup arr_id vtab) of
                                                        SOME m => m
                                                      | NONE   => raise NotInSymTab(arr_id)
                          val meincl = map (fn (idarr, fcomps) => (idarr, fid::fcomps)) mapparinf
                          val () = if(length(mapparinf) <> 0) then (  (*ct := !ct + 1;*) ctm := !ctm+1) else ()
                      in  (arr_id, [fid])::meincl
                      end
                | otherwise => []

              val (vtab1, ok1) = map_fusion_gather( e1, (vtab,ok) )
              val  vtab2       = SymTab.bind id (exp, count, countmap, mapinf) vtab1
              val (vtab3, ok3) = map_fusion_gather( e2, (vtab2,ok1) )

              val (mye,myct,myctmap,myinf) = case (SymTab.lookup id vtab3) of
                                               SOME m => m
                                             | NONE   => raise NotInSymTab(id)

              val succ = (!myct = !myctmap andalso !myctmap = 1)
              val ()   = if(succ) then myct := ~1 else ()
          in (vtab3, succ orelse ok3)
          end

       | Band (e1, e2, p) =>  map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | Plus (e1, e2, p) =>  map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | Minus(e1, e2, p) =>  map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | Equal(e1, e2, p) =>  map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | Less (e1, e2, p) =>  map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | If(e1, e2, e3, p)=>  map_fusion_gather(e3, map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok))))
       | Apply(id,args,p) =>  foldl (fn(x,y)=> map_fusion_gather(x,y)) (vtab,ok) args
       | Map(id,e,t1,t2,p)=>  map_fusion_gather(e, (vtab,ok))
       | Reduce(id,e,l,t,p)=> map_fusion_gather(l, map_fusion_gather(e, (vtab,ok)))
       | Iota (e, p)      =>  map_fusion_gather(e, (vtab,ok))
       | Replicate(e,n,t,p)=> map_fusion_gather(e, map_fusion_gather(n, (vtab,ok)))
       | Concat(e1,e2,t,p)=> map_fusion_gather(e1, map_fusion_gather(e2, (vtab,ok)))
       | Split(e1,e2,t,p)=> map_fusion_gather(e1, map_fusion_gather(e2, (vtab,ok)))
       | Write (e,t,p)    =>  map_fusion_gather(e, (vtab,ok))
       | Index(id,e,t,p)  =>
            let val (e1,ct,ctm,mapparinf) = case (SymTab.lookup id vtab) of
                                              SOME m => m
                                            | NONE   => raise NotInSymTab(id)
                val () = ct := !ct + 1
            in  map_fusion_gather(e, (vtab,ok))
            end
       | Var(id,p) =>
            let val (e1,ct,ctm,mapparinf) = case (SymTab.lookup id vtab) of
                                              SOME m => m
                                            | NONE   => raise NotInSymTab(id)
                val () = ct := !ct + 1
            in (vtab, ok)
            end


       | Times (e1,e2,p) => map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | Divide(e1,e2,p) => map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | And   (e1,e2,p) => map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | Or    (e1,e2,p) => map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))
       | Not   (e,p)     => map_fusion_gather(e, (vtab,ok))
       | Negate(e,p)     => map_fusion_gather(e, (vtab,ok))

       | Scan  (id,e,l,t,p)=> map_fusion_gather(l, map_fusion_gather(e, (vtab,ok)))
       | ZipWith(id, e1, e2, t1, t2, t3, p) => map_fusion_gather(e2, map_fusion_gather(e1, (vtab,ok)))

       | ArrayLit (lst,tp,pos) => foldl (fn(x,y)=> map_fusion_gather(x,y)) (vtab,ok) lst

       | Read     (t,p) => (vtab, ok)
       | Num      (n,p) => (vtab, ok)
       | Log      (b,p) => (vtab, ok)
       | CharLit  (c,p) => (vtab, ok)
       | StringLit(s,p) => (vtab, ok)

       | _ => raise Error("map_fusion_gather: Unimplemented AbSyn node", (0,0))
(*       | _ => (vtab, ok) *)

  (**********************************************************)
  (** Bottom-up parsing constructs the map-fused AbSyn     **)
  (** A fused node deletes itself as fusion makes it unused**)
  (** The maximal fusion is chosen, under the conservative **)
  (** condition that the fused array is not used anywhere. **)
  (** Returns an abstract syntax tree with map-fused nodes **)
  (**********************************************************)

  fun make_funcomp( funids, funs : FunDec list ) : (FunDec * string) =
    let fun make_funcomp_exp(  [],   elid ) = (Fasto.Var(elid, (0,0)), "_ft_exp_"^newName())
          | make_funcomp_exp( f::fs, elid ) =
               let val (exp, fid) = make_funcomp_exp(fs, elid)
                   val let_var = "ft_var_"^newName()
               in  ( Fasto.Let( Fasto.Dec  ( let_var, exp, (0,0) ),
                                Fasto.Apply( f, [Fasto.Var(let_var,(0,0))], (0,0) ),
                                (0,0)
                              )
                   , f^"o"^fid
                   )
                   (***   ( Fasto.Apply( f, [exp], (0,0) ), f^"o"^fid )    ***)
               end
        fun find_id(fid) =
            case List.find (fn (ff, tp, ag, e, p) => ff = fid ) funs of
              SOME m => m
            | NONE   => raise Error("Function: "^fid^" not found in FunDecs!", (0,0))

        val elid      = "arg__ft_exp_"^newName()
        val (exp,fid) = make_funcomp_exp(funids, elid)
        val (f1, rtp , a1, body1, p1) = find_id(hd(funids))
        val (f2, rtp2, a2, body2, p2) = find_id(List.last(funids))
        val (f2argname, intp) = hd a2
        val newfun = (fid, rtp, [(elid, intp)], exp, (0,0))
    in (newfun, fid)
    end

  (** (string * string list) list -> (string * string list)  **)
(*
  fun select_max_fusion([], vtab) =
        raise Error("Error in map_fusion_gen. Info should be non-empty!",(0,0))
    | select_max_fusion( [(arrid,fcomps)], vtab ) = (arrid, fcomps)
    | select_max_fusion( (id1,fcomps1)::(id2,fcomps2)::info, vtab ) =
        let val (e1,ct,ctm,mapparinf) = case (SymTab.lookup id2 vtab) of
                                          SOME m => m
                                        | NONE   => raise NotInSymTab(id2)
        in  if(!ct = ~1) then (id1,fcomps1)
            else  select_max_fusion((id2,fcomps2)::info, vtab)
        end
*)

  fun select_max_fusion([], vtab, prev) = prev
    | select_max_fusion((id1,fcomps1)::info, vtab, (id2,fcomps2)) =
        let val (e1,ct,ctm,mapparinf) = case (SymTab.lookup id2 vtab) of
                                               SOME m => m
                                             | NONE   => raise NotInSymTab(id2)
        in if(!ct = ~1) then select_max_fusion(info, vtab, (id1, fcomps1))
                        else (id2, fcomps2)
        end

  fun map_fusion_gen(exp : Exp, funs : FunDec list, vtab) : (Exp * FunDec list) =
    case exp of
      Let( Dec(id,e1,p1), e2, p2 ) =>
          let val (e11, f11) = map_fusion_gen(e1, funs, vtab)
              val (e22, f22) = map_fusion_gen(e2, funs, vtab)
              val (mye,myct,myctmap,myinf) = case (SymTab.lookup id vtab) of
                                               SOME m => m
                                             | NONE   => raise NotInSymTab(id)
              val refcount = !myct
          in  case refcount of
                ~1 => (e22, f22)
              | _  => if(myinf = []) then ( Let( Dec(id,e11,p1), e22, p2 ), f11@f22)
                      else let val (newarr, flst) = select_max_fusion(tl myinf, vtab, hd myinf) (* select_max_fusion(List.rev myinf, vtab) *)
                           in  if(length(flst) < 2) then ( Let( Dec(id,e11,p1), e22, p2 ), f11@f22 )  (*>*)
                               else let val (newfun, newfid) = make_funcomp( flst, funs )
                                        val (rtp,intp) = case newfun of
                                                           (fffid, rettp, [(elid, inptp)], eee, ppp) => (rettp,inptp)
                                                         | otherwise => raise Error("Map fusion failed, illegal return of make_funcomp!",p2)
                                    in case e11 of
                                         Map(fid, Var(arr_id,p), t1, t2, pos) =>
                                             ( Let( Dec(id, Map(newfid,Var(newarr,p), intp, rtp, pos), p1), e22, p2),   (** t1,t2 **)
                                               newfun::(f11@f22) )
                                       | otherwise => raise Error("Error in map_fusion_gen: Can't find MAP-FUSED NODE!",(0,0))
                                    end
                           end
          end

    | Band (e1, e2, p) =>  let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                               val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                           in  ( Band (e11, e22, p), f11@f22 )
                           end
    | Plus (e1, e2, p) =>  let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                               val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                           in  ( Plus (e11, e22, p), f11@f22 )
                           end
    | Minus(e1, e2, p) =>  let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                               val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                           in  ( Minus(e11, e22, p), f11@f22 )
                           end

    | Equal(e1, e2, p) =>  let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                               val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                           in  ( Equal(e11, e22, p), f11@f22 )
                           end
    | Less (e1, e2, p) =>  let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                               val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                           in  ( Less(e11, e22, p), f11@f22 )
                           end
    | If(e1, e2, e3, p)=>  let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                               val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                               val (e33,f33) = map_fusion_gen(e3, funs, vtab)
                           in  ( If(e11, e22, e33, p), f11@f22@f33 )
                           end
    | Apply(id,args,p) =>  let val resss = map (fn x=>map_fusion_gen(x,funs,vtab)) args
                               val targs = map (fn (x,y) => x) resss
                               val tffs  = map (fn (x,y) => y) resss
                           in ( Apply(id, targs, p), foldl (op @) [] tffs )
                           end
    | Map(id,e,t1,t2,p)=>  let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                           in  ( Map(id, e11, t1, t2, p), f11 )
                           end
    | Reduce(id,e,l,t,p)=> let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                               val (e22,f22) = map_fusion_gen(l, funs, vtab)
                           in  ( Reduce(id, e11, e22, t, p), f11@f22 )
                           end
    | Iota (e, p)      =>  let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                           in  ( Iota(e11, p), f11 )
                           end
    | Split(e1,e2,t,p)=> let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                               val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                           in  ( Split(e11, e22, t, p), f11@f22 )
                           end
    | Concat(e1,e2,t,p)=> let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                               val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                           in  ( Concat(e11, e22, t, p), f11@f22 )
                           end
    | Replicate(e,n,t,p)=> let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                               val (e22,f22) = map_fusion_gen(n, funs, vtab)
                           in  ( Replicate(e11, e22, t, p), f11@f22 )
                           end
    | Write (e,t,p)    =>  let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                           in  ( Write(e11, t, p), f11 )
                           end
    | Index(id,e,t,p)  =>  let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                           in  ( Index(id, e11, t, p), f11 )
                           end

       | Times (e1,e2,p) => let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                                val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                            in  ( Times (e11, e22, p), f11@f22 )
                            end
       | Divide(e1,e2,p) => let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                                val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                            in  ( Divide (e11, e22, p), f11@f22 )
                            end
       | And   (e1,e2,p) => let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                                val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                            in  ( And (e11, e22, p), f11@f22 )
                            end
       | Or    (e1,e2,p) => let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                                val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                            in  ( Or (e11, e22, p), f11@f22 )
                            end
       | Not   (e,p)     => let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                            in  ( Not(e11, p), f11 )
                            end
       | Negate(e,p)     => let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                            in  ( Negate(e11, p), f11 )
                            end
       | Scan  (id,e,l,t,p)=> let val (e11,f11) = map_fusion_gen(e, funs, vtab)
                                  val (e22,f22) = map_fusion_gen(l, funs, vtab)
                              in  ( Scan(id, e11, e22, t, p), f11@f22 )
                              end
       | ZipWith(id, e1, e2, t1, t2, t3, p) =>
                            let val (e11,f11) = map_fusion_gen(e1, funs, vtab)
                                val (e22,f22) = map_fusion_gen(e2, funs, vtab)
                            in  ( ZipWith (id, e11, e22, t1, t2, t3, p), f11@f22 )
                            end
       | ArrayLit (lst,tp,pos) =>
                            let val resss = map (fn x=>map_fusion_gen(x,funs,vtab)) lst
                                val targs = map (fn (x,y) => x) resss
                                val tffs  = map (fn (x,y) => y) resss
                            in ( ArrayLit(targs, tp, pos), foldl (op @) [] tffs )
                            end
       | Var      (a,p) => (exp, [])
       | Read     (t,p) => (exp, [])
       | Num      (n,p) => (exp, [])
       | Log      (b,p) => (exp, [])
       | CharLit  (c,p) => (exp, [])
       | StringLit(s,p) => (exp, [])

       | _ => raise Error("map_fusion_gen: Unimplemented AbSyn node!", (0,0))
(*    | _ => (exp, []) *)


  (*************************************)
  (** Finally, Map-Fusion Driver      **)
  (*************************************)
  fun mapfusion_driver( funs : FunDec list ) : (bool * FunDec list) =
    let fun fusion (fid, rtp, fargs, body, pdecl) =
            let val vtab_ini = map (fn (x,y) => (x, (Var(x,(0,0)), ref 0, ref 0, [])) ) fargs
                val (vtab,succ)   = map_fusion_gather(body, (vtab_ini,false))
                val (nbody,nfuns) = map_fusion_gen(body, funs, vtab)
                val curr_fun      = (fid, rtp, fargs, nbody, pdecl)
            in (succ, curr_fun::nfuns)
            end
        val outs   = map fusion funs

        val succs  = map (fn (x,y)=>x) outs
        val succ   = List.foldl (fn (a,b) => a orelse b) false succs
        val newfuns= List.concat( map (fn (x,y)=>y) outs )
    in (succ, newfuns)
(*
    in  if(succ) then let val (s, itres) = mapfusion_driver( newfuns ) in (true,itres) end
        else (succ, newfuns)
*)

    end

  (*************************************************************)
  (*************************************************************)
  (*** 1. Function Inlining                                  ***)
  (*** inline(e : Fasto.Exp) : Fasto.Exp                     ***)
  (*************************************************************)
  (*************************************************************)

  (* Helpers for Inlining*)

  fun find_id_funlst(lst, []  ) = []
    | find_id_funlst(lst, (fid, rtp, fargs, body, pdecl)::fs) =
       ( case( List.find (fn x => x = fid) lst ) of
           NONE   => find_id_funlst(lst, fs)
         | SOME m => [(fid, rtp, fargs, body, pdecl)]
       )

  fun checkVarsOnly([]   ) : bool = true
    | checkVarsOnly(e::es) : bool = (case e of Var(id,pos) => checkVarsOnly(es) | _ => false)



  (** Builds lst1 and lst2: all the functions that are used, and the functions that are used from Apply nodes **)
  fun find_callees(e : Exp, (lst1 : string list, lst2 : string list)) : (string list * string list) =
    let fun find_explst([],   (lst1, lst2)) = (lst1, lst2)
          | find_explst(e::es,(lst1, lst2)) = find_explst( es, find_callees(e, (lst1, lst2)) )
    in case e of
         Let( Dec(id, e1, pos1), e2, pos2 ) => find_callees(e2, find_callees(e1, (lst1,lst2)))
       | Band (e1, e2, pos) =>  find_callees(e2, find_callees(e1, (lst1,lst2)))
       | Plus (e1, e2, pos) =>  find_callees(e2, find_callees(e1, (lst1,lst2)))
       | Minus(e1, e2, pos) =>  find_callees(e2, find_callees(e1, (lst1,lst2)))
       | Equal(e1, e2, pos) =>  find_callees(e2, find_callees(e1, (lst1,lst2)))
       | Less (e1, e2, pos) =>  find_callees(e2, find_callees(e1, (lst1,lst2)))
       | If(e1, e2, e3, pos)=>  find_callees(e3, find_callees(e2, find_callees(e1, (lst1,lst2))))
       | Apply(id,args,pos) =>  let val new_lst = case List.find (fn x => x = id) lst1 of
                                                NONE   => ( id::lst1, id::lst2 )
                                              | SOME m => ( lst1, lst2 )
                                in find_explst(args, new_lst)
                                end

       | Map(id,e,t1,t2,pos)=>  (*** we aggresively inline Map nodes => ***)
                                (***          beneficial for map fusion ***)
                                find_callees(e, (lst1,lst2))

(*                              (*** conservative inlining considers treats function references ***)
                                (*** from inside a Map node as a function call                  ***)
                                let val new_lst = case List.find (fn x => x = id) lst1 of
                                                NONE   => (id :: lst1, lst2)
                                              | SOME m => ( lst1, lst2 )
                                in find_callees(e, new_lst)
                                end
*)
       | Reduce(id,e,lst,t,p)=>(*** we aggresively inline Reduce nodes => ***)
                               (***      beneficial for map-reduce fusion ***)
                               find_callees(e, find_callees(lst, (lst1,lst2)))
(*                             (*** conservative inlining considers treats function references ***)
                               (*** from inside a Map node as a function call                  ***)
                               let val new_lst = case List.find (fn x => x = id) lst1 of
                                                NONE   => (id :: lst1, lst2)
                                              | SOME m => (lst1, lst2)
                               in find_callees(e, new_lst)
                               end
*)
       | Index(id,e,t,pos)     =>  find_callees(e, (lst1,lst2))
       | Iota (e, pos)         =>  find_callees(e, (lst1,lst2))
       | Concat(e1,e2,t,pos) =>  find_callees(e1, find_callees(e2, (lst1,lst2)))
       | Split(e1,e2,t,pos) =>  find_callees(e1, find_callees(e2, (lst1,lst2)))
       | Replicate(e,el,t,pos) =>  find_callees(e, find_callees(el, (lst1,lst2)))
       | Write (e,t,pos)       =>  (* should I allow things to be inlined inside write??? *)
                                   find_callees(e, (lst1,lst2))

       | Times (e1,e2,p) => find_callees(e2, find_callees(e1, (lst1,lst2)))
       | Divide(e1,e2,p) => find_callees(e2, find_callees(e1, (lst1,lst2)))
       | And   (e1,e2,p) => find_callees(e2, find_callees(e1, (lst1,lst2)))
       | Or    (e1,e2,p) => find_callees(e2, find_callees(e1, (lst1,lst2)))
       | Not   (e,p)     => find_callees(e, (lst1,lst2))
       | Negate(e,p)     => find_callees(e, (lst1,lst2))

       | Scan  (id,e,l,t,p)=> find_callees(e, find_callees(l, (lst1,lst2)))
       | ZipWith(id, e1, e2, t1, t2, t3, p) => find_callees(e2, find_callees(e1, (lst1,lst2)))

       | ArrayLit (lst,tp,pos) => foldr (fn(x,y)=> find_callees(x,y)) (lst1,lst2) lst
                                    (* should I allow things to be inlined inside array literals??? *)

       | Var      (v,p) => (lst1,lst2)
       | Read     (t,p) => (lst1,lst2)
       | Num      (n,p) => (lst1,lst2)
       | Log      (b,p) => (lst1,lst2)
       | CharLit  (c,p) => (lst1,lst2)
       | StringLit(s,p) => (lst1,lst2)

       | _ => raise Error("find_callees: Unimplemented AbSyn node!", (0,0))
(*       | _ => (lst1,lst2)     *)
    end



  (******************************************************************)
  (***  Performing the substitution of arguments with fresh names ***)
  (******************************************************************)
  fun subs_vars_in_exp( exp : Exp, nms : (string * string) list ) : Exp = case exp of
         Let( Dec(id,e1,p1), e2, p2 ) =>
            Let( Dec(id, subs_vars_in_exp(e1, nms), p1 ),
            subs_vars_in_exp(e2, nms), p2
         )
       | Band (e1, e2, p) =>  Band ( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms),  p )
       | Plus (e1, e2, p) =>  Plus ( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms),  p )
       | Minus(e1, e2, p) =>  Minus( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms),  p )
       | Equal(e1, e2, p) =>  Equal( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms), p )
       | Less (e1, e2, p) =>  Less ( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms),  p )
       | If(e1, e2, e3, p)=>  If   ( subs_vars_in_exp(e1, nms),
                                     subs_vars_in_exp(e2, nms),
                                     subs_vars_in_exp(e3, nms), p )
       | Apply(id,args,p) => Apply ( id, map (fn x => subs_vars_in_exp(x, nms)) args,  p )
       | Map(id,e,t1,t2,p)=> Map   ( id, subs_vars_in_exp(e, nms), t1, t2, p )
       | Reduce(id,e,l,t,p)=>Reduce( id, subs_vars_in_exp(e, nms), subs_vars_in_exp(l, nms), t, p )
       | Iota (e, p)      => Iota  ( subs_vars_in_exp(e, nms), p )
       | Replicate(e,n,t,p)=>Replicate( subs_vars_in_exp(e, nms), subs_vars_in_exp(n, nms), t, p )
       | Concat(e1,e2,t,p)=>Concat( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms), t, p )
       | Split(e1,e2,t,p)=>Split( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms), t, p )
       | Write (e,t,p)    => Write ( subs_vars_in_exp(e, nms), t, p )
       | Index(id,e,t,p)  =>
            let val filt = List.filter (fn (x,y) => x = id) nms
                val new_e = subs_vars_in_exp(e, nms)
                in  case filt of
                      []                   => Index(id,    new_e, t, p)
                    | (oldid, newid)::rest => Index(newid, new_e, t, p)
            end
       | Var(id,p) =>
            let val filt = List.filter (fn (x,y) => x = id) nms
            in case filt of
                      []                   => Var(id,    p)
                    | (oldid, newid)::rest => Var(newid, p)
            end

       | Times (e1,e2,p) => Times ( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms),  p )
       | Divide(e1,e2,p) => Divide( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms),  p )
       | And   (e1,e2,p) => And   ( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms),  p )
       | Or    (e1,e2,p) => Or    ( subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms),  p )
       | Not   (e,p)     => Not   ( subs_vars_in_exp(e, nms), p )
       | Negate(e,p)     => Negate( subs_vars_in_exp(e, nms), p )

       | ZipWith(id,e1,e2,t1,t2,t3,p)=>ZipWith(id, subs_vars_in_exp(e1, nms), subs_vars_in_exp(e2, nms), t1, t2, t3, p )
       | Scan   (id,e,l,t,p) => Scan( id, subs_vars_in_exp(e, nms), subs_vars_in_exp(l, nms), t, p )

       | ArrayLit (lst,tp,pos) => ArrayLit ( map (fn x => subs_vars_in_exp(x, nms)) lst,  tp, pos )

       | Read     (t,p) => exp
       | Num      (n,p) => exp
       | Log      (b,p) => exp
       | CharLit  (c,p) => exp
       | StringLit(s,p) => exp

       | _ => raise Error("subst_vars_in_exps: Unimplemented AbSyn node!", (0,0))
(*       | _ => exp              *)

  fun subs_vars_in_fun( [],  (fid, rtp, fargs, nbody, pdecl), new_names ) : Exp =
        subs_vars_in_exp(nbody, new_names)
    | subs_vars_in_fun( aa,  (fid, rtp, fargs, nbody, pdecl), new_names ) : Exp =
        let val (fa,tp) = hd(fargs)
            val  new_fa  = fa^"_ft_exp_"^newName()
            val  new_exp = subs_vars_in_fun(tl(aa), (fid,rtp,tl(fargs),nbody,pdecl), (fa,new_fa)::new_names)
        in Let( Dec(new_fa, hd(aa), pdecl), new_exp, pdecl) end


  fun inline_in_body(exp : Exp, tobeinl : FunDec list, succ : bool) : (bool * Exp) =
    case exp of
      Let( Dec(id, e1, pos1), e2, pos2 ) => let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                                val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                                            in (s1 orelse s2 orelse succ, Let( Dec(id, e11, pos1), e22, pos2 ) )
                                            end
    | Band (e1, e2, pos) =>  let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                 val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                             in  (s1 orelse s2 orelse succ, Band( e11, e22, pos ) )
                             end
    | Plus (e1, e2, pos) =>  let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                 val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                             in  (s1 orelse s2 orelse succ, Plus( e11, e22, pos ) )
                             end
    | Minus(e1, e2, pos) =>  let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                 val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                             in  (s1 orelse s2 orelse succ, Minus( e11, e22, pos ) )
                             end
    | Equal(e1, e2, pos) =>  let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                 val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                             in  (s1 orelse s2 orelse succ, Equal( e11, e22, pos ) )
                             end
    | Less (e1, e2, pos) =>  let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                 val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                             in  (s1 orelse s2 orelse succ, Less( e11, e22, pos ) )
                             end

    | If(e1, e2, e3, pos)=>  let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                 val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                                 val (s3,e33) = inline_in_body(e3, tobeinl, succ)
                             in  (s1 orelse s2 orelse s3 orelse succ, If( e11, e22, e33, pos ) )
                             end
    | Apply(id,args,pos) =>  let val callee  = find_id_funlst([id], tobeinl)
                                 val argres  = map (fn x => inline_in_body(x, tobeinl, succ)) args
                                 val new_arg = map (fn (b,x) => x) argres
                                 val new_succ= foldl (fn (x,y) => x orelse y) succ (map (fn (b,x)=>b) argres)
                             in if(callee = []) then ( new_succ, Apply(id, new_arg,pos) )
                                else (* HERE WE DO INLINING *)
                                     if (checkVarsOnly(args) = false )
                                     then ( print(pp_exp 0 exp); raise Error("Unormlized Code in Inline; Exits!", (0,0))  )
                                     else ( true, subs_vars_in_fun(args, hd(callee), []) )
                             end
    | Map(id,e,t1,t2,pos)=>  let val (s1,e11) = inline_in_body(e, tobeinl, succ)
                             in (s1 orelse succ, Map(id,e11,t1,t2,pos))
                             end
    | Reduce(id,e,lst,t,p)=> let val (s1,e11) = inline_in_body(e,   tobeinl, succ)
                                 val (s2,e22) = inline_in_body(lst, tobeinl, succ)
                             in (s1 orelse s2 orelse succ, Reduce(id,e11, e22, t, p) )
                             end
    | Index(id,e,t,pos)     =>  let val (s1,e11) = inline_in_body(e, tobeinl, succ) in (s1, Index(id,e11,t,pos)) end
    | Iota (e, pos)         =>  let val (s1,e11) = inline_in_body(e, tobeinl, succ) in (s1, Iota(e11,pos)) end
    | Concat(e1,e2,t,pos) =>  let val (s1,e11) = inline_in_body(e1,  tobeinl, succ)
                                    val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                                in (s1 orelse s2 orelse succ, Concat(e11, e22, t, pos) )
                                end
    | Split(e1,e2,t,pos) =>  let val (s1,e11) = inline_in_body(e1,  tobeinl, succ)
                                    val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                                in (s1 orelse s2 orelse succ, Split(e11, e22, t, pos) )
                                end
    | Replicate(e,el,t,pos) =>  let val (s1,e11) = inline_in_body(e,  tobeinl, succ)
                                    val (s2,e22) = inline_in_body(el, tobeinl, succ)
                                in (s1 orelse s2 orelse succ, Replicate(e11, e22, t, pos) )
                                end
    | Write (e,t,pos)       =>  let val (s1,e11) = inline_in_body(e, tobeinl, succ) in (s1, Write(e11,t,pos)) end


        | ZipWith(id,e1,e2,t1,t2,t3,p)=>
                            let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                            in (s1 orelse s2 orelse succ, ZipWith( id, e11, e22, t1, t2, t3, p ) )
                            end
        | Scan(id,e,lst,t,p)=> let val (s1,e11) = inline_in_body(e,   tobeinl, succ)
                                   val (s2,e22) = inline_in_body(lst, tobeinl, succ)
                               in (s1 orelse s2 orelse succ, Scan(id,e11, e22, t, p) )
                               end

       | Times (e1,e2,p) => let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                            in (s1 orelse s2 orelse succ, Times( e11, e22, p ) )
                            end
       | Divide(e1,e2,p) => let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                            in (s1 orelse s2 orelse succ, Divide( e11, e22, p ) )
                            end
       | And   (e1,e2,p) => let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                            in (s1 orelse s2 orelse succ, And( e11, e22, p ) )
                            end
       | Or    (e1,e2,p) => let val (s1,e11) = inline_in_body(e1, tobeinl, succ)
                                val (s2,e22) = inline_in_body(e2, tobeinl, succ)
                            in (s1 orelse s2 orelse succ, Or( e11, e22, p ) )
                            end
       | Not   (e,p)     => let val (s1,e11) = inline_in_body(e, tobeinl, succ)
                            in (s1 orelse succ, Not(e11,p))
                            end
       | Negate(e,p)     => let val (s1,e11) = inline_in_body(e, tobeinl, succ)
                            in (s1 orelse succ, Negate(e11,p))
                            end

       | ArrayLit (lst,tp,pos) => (succ,exp)
       | Var(v,p) => (succ, exp)
       | Read     (t,p) => (succ,exp)
       | Num      (n,p) => (succ,exp)
       | Log      (b,p) => (succ,exp)
       | CharLit  (c,p) => (succ,exp)
       | StringLit(s,p) => (succ,exp)

       | _ => raise Error("inline_in_body: Unimplemented AbSyn node!", (0,0))
(*    | _ => (succ,exp)     *)


  fun inline_in_caller( ((fid, rtp, fargs, body, pdecl), (l1,l2)), tobeinl ) : (bool * FunDec) =
      let val fails = find_id_funlst(l2, tobeinl)
      in if(fails=[]) then (false, (fid, rtp, fargs, body, pdecl))
         else let val (succ, nbody) = inline_in_body(body, tobeinl, false)
              in  if(succ) then (succ, (fid, rtp, fargs, nbody, pdecl))
                  else raise Error("Inlining Have Failed: Broken Invariant in inline_in_caller!", (0,0))
              end
      end



  (***************************************************************)
  (*** Function Inlining Driver:                               ***)
  (*** 1. `tobeinl' are the functions that do not call others  ***)
  (*** 2. `others' are funs that might call others (map/apply) ***)
  (*** 3. attempt to inline `tobeinl' funs in `others' funs    ***)
  (*** 4. repeat to a fixed point                              ***)
  (***************************************************************)
  fun inline_driver(funlst : FunDec list, succ : bool) : (bool * FunDec list) =
      let val callees = map ( fn (fid, rtp, fargs, body, pdecl)
                                => ( (fid, rtp, fargs, body, pdecl), find_callees(body, ([],[]) )) )
                             funlst
          val tobeinl = map (fn (f,(l1,l2)) =>f) (List.filter (fn (f,(l1,l2)) => (l1 = [])) callees)
          val others  = List.filter (fn (f, (l1,l2)) => (l1 <> [])) callees
          val partres = map (fn x => inline_in_caller(x,tobeinl)) others
          val bools   = map (fn(b,f) => b ) partres
          val res     = map (fn(b,f) => f ) partres
      in if(List.foldl (fn (a,b) => a orelse b) false bools)
         then let val (b,fs) = inline_driver(res, true)
              in ( true, tobeinl@fs )
              end
         else ( succ, funlst )
      end



  (*************************************************************)
  (*************************************************************)
  (*** 1. LET-NORMALIZATION Optimization                     ***)
  (*** let_norm_exp(e : Fasto.Exp) : Fasto.Exp               ***)
  (*************************************************************)
  (*************************************************************)

  (* treats the case Let x = y in exp => subst(x|y, exp) *)
  fun elim_redundancy(exp) = case exp of
        Let( Dec(id1, Var(id2,p), pos1), ine, pos2 ) =>
            elim_redundancy( subs_vars_in_exp( ine, [(id1,id2)] ) )
      | otherwise => exp


  fun let_norm_exp( e:Fasto.Exp ) : Fasto.Exp= case e of
    Let( Dec(id, e1, pos1), e2, pos2 ) =>
        let val el_red  = elim_redundancy(e)
        in if(el_red <> e) then el_red
           else
            let val e1_norm  = let_norm_exp(e1)
                val e2_norm2 = let_norm_exp(e2)
                val e2_norm  = case e2_norm2 of
                                 Map(s,emap,t1,t2,p) =>
                                     let val nm = "ft_map_"^newName()
                                     in Let(Dec(nm, e2_norm2,p), Var(nm,p), p) end
                               | otherwise => e2_norm2
            in case e1_norm of
                 Let( Dec(id1, e11, pos11), e12, pos12 ) =>
                   let val inner = let_norm_exp( Let( Dec(id, e12, pos1), e2_norm, pos2 ) )
                   in  Let( Dec(id1, e11, pos11), inner, pos12 )
                   end
               | _ => if(e1_norm = e1 andalso e2_norm = e2) then e
                      else  Let( Dec(id, e1_norm, pos1), e2_norm, pos2 )
            end
        end

  | Band(e1, e2, pos) =>  let fun f(l,p) = Band (List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end
  | Plus(e1, e2, pos) =>  let fun f(l,p) = Plus (List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end
  | Minus(e1, e2, pos)=>  let fun f(l,p) = Minus(List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end

  | Equal(e1, e2, pos)=>  let fun f(l,p) = Equal(List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end

  | Less(e1, e2, pos) =>  let fun f(l,p) = Less (List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end

  | If(e1, e2, e3, pos) => let fun f(l,p) = If(hd(l), let_norm_exp(e2), let_norm_exp(e3), pos)
                           in  bind_exps ([e1], [], f, pos)
                           end

  | Apply(id,args,pos) => let fun f(l,p) = Apply(id,l,p)
                          in  bind_exps (args, [], f, pos)
                          end

(*
                             let val new_tmp = "ft_exp_"^newName()
                                 fun f(l,p) = Let (  Dec(new_tmp,Apply(id,l,p), p),
                                                     Var(new_tmp, p), p
                                                  )
                             in  bind_exps(args, [], f, pos)
                             end
*)

  | Index(id,e,t,pos) => let fun f(l,p) = Index(id,hd(l),t,p)
                         in  bind_exps ([e], [], f, pos)
                         end (* Index(id, let_norm_exp(e), t, pos) *)

  | Iota (e, pos)     => let fun f(l,p) = Iota(hd(l),p)
                         in  bind_exps([e], [], f, pos)
                         end

  | Concat(e1,e2,t,pos) => let fun f(l,p) = Concat(List.nth(l,0),List.nth(l,1),t,p)
                             in  bind_exps([e1,e1], [], f, pos)
                             end
  | Split(e1,e2,t,pos) => let fun f(l,p) = Split(List.nth(l,0),List.nth(l,1),t,p)
                             in  bind_exps([e1,e1], [], f, pos)
                             end
  | Replicate(e,el,t,pos) => let fun f(l,p) = Replicate(List.nth(l,0),List.nth(l,1),t,p)
                             in  bind_exps([e,el], [], f, pos)
                             end

  | Map(id,e,t1,t2,pos)   => let fun f(l,p) = Map(id,hd(l),t1,t2,p)
                             in  bind_exps([e], [], f, pos)
                             end

  | Reduce(id,e,lst,t,pos)=> let fun f(l,p) = Reduce(id,List.nth(l,0),List.nth(l,1),t,p)
                             in  bind_exps([e,lst], [], f, pos)
                             end
  | Write (e,t,pos)   => let fun f(l,p) = Write(hd(l),t,p) in bind_exps([e], [], f, pos) end



  | Times (e1,e2,pos) =>  let fun f(l,p) = Times (List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end
  | Divide(e1,e2,pos) =>  let fun f(l,p) = Divide(List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end
  | And(e1,e2,pos)    =>  let fun f(l,p) = And (List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end
  | Or (e1,e2,pos)    =>  let fun f(l,p) = Or  (List.nth(l,0), List.nth(l,1),p)
                          in  bind_exps ([e1,e2], [], f, pos)
                          end
  | Negate(e,pos)     =>  let fun f(l,p) = Negate(hd(l),p)
                          in  bind_exps([e], [], f, pos)
                          end
  | Not(e,pos)        =>  let fun f(l,p) = Not(hd(l),p)
                          in  bind_exps([e], [], f, pos)
                          end
  | Scan (id,e,lst,t,pos) => let fun f(l,p) = Scan(id,List.nth(l,0),List.nth(l,1),t,p)
                             in  bind_exps([e,lst], [], f, pos)
                             end
  |ZipWith(id,e1,e2,t1,t2,t3,pos) =>
                        let fun f(l,p) = ZipWith (id, List.nth(l,0), List.nth(l,1), t1, t2, t3, p)
                        in  bind_exps ([e1,e2], [], f, pos)
                        end

       | ArrayLit (lst,tp,pos) => e
       | Var(v,p) => e
       | Read     (t,p) => e
       | Num      (n,p) => e
       | Log      (b,p) => e
       | CharLit  (c,p) => e
       | StringLit(s,p) => e

       | _ => raise Error("let_norm_exp: Unimplemented AbSyn node!", (0,0))

(*  | _ => e     *)


  (*******************************************************************)
  (** Helper function for normalization: Parameters:                **)
  (**   1. list of expressions,                                     **)
  (**   2. an initially empty list of variables, i.e., Var(id,pos)  **)
  (**   3. a function that should create the desired innermost exp, **)
  (**        e.g., in case of Equal:                                **)
  (**          fun f(l,p) = Equal(List.nth(l,0), List.nth(l,1), p)  **)
  (**   4. the position in the code                                 **)
  (** The result is a normalized Let expression,                    **)
  (**       i.e., three-address-like code                           **)
  (*******************************************************************)
  and bind_exps ([],    vars, f, pos) = f( List.rev(vars), pos )
    | bind_exps (e::es, vars, f, pos) = ( case e of
            Var(id,pos) => bind_exps(es, e::vars, f, pos)
         |  _           =>
              let val new_nm = "ft_exp_"^newName()
                  val new_var= Var(new_nm, pos)
                  val e_norm = let_norm_exp(e)
                  val new_e  = bind_exps(es, new_var::vars, f, pos)

              in case e_norm of
                   Let( Dec(id, l_e, pos1), in_e, pos2 ) =>
                       let val inner = let_norm_exp(
                               Let(Dec(new_nm, in_e, pos2), new_e, pos2) )
                       in  Let( Dec(id, l_e, pos1), inner, pos )
                       end
                 | _ => Let(Dec(new_nm, e_norm, pos), new_e, pos)
              end
        )

  (*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*)
  (*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*)
  (*!!!!!!!!!!!! EXAM's OPTIMIZATIONS !!!!!!!!!!*)
  (*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*)
  (*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*)

  (***********)
  (* HELPERS *)
  (***********)

fun isCtVal( exp ) = case exp of
        Num      (n, p) => true
      | Log      (b, p) => true
      | CharLit  (c, p) => true
      | otherwise       => false

fun isCpVar( exp ) = case exp of
        Var(id, p) => true
      | otherwise  => false

fun evalBinop ( bop, Num(n1,p1), Num(n2, p2), pos ) = Num(bop(n1, n2),pos)
  | evalBinop ( bop, e1, e2, pos ) =
        raise Error("Arguments of + Are Not Integers! Arg1: " ^
                    pp_exp 0 e1 ^ " Arg2: " ^ pp_exp 0 e2, pos )

fun evalEq ( Num(n1,p1),     Num(n2,p2),     pos ) = Log(n1=n2,pos)
  | evalEq ( Log(b1,p1),     Log(b2,p2),     pos ) = Log(b1=b2,pos)
  | evalEq ( CharLit(c1,p1), CharLit(c2,p2), pos ) = Log(c1=c2,pos)
  | evalEq ( e1, e2, pos ) = raise Error("Argument Types Do Not Match! Arg1: " ^
                                          pp_exp 0 e1 ^ " Arg2: " ^ pp_exp 0 e2, pos )

fun evalRelop ( bop, Num(n1,p1),     Num(n2,p2),     pos ) = Log(bop(n1,n2),pos)
  | evalRelop ( bop, CharLit(n1,p1), CharLit(n2,p2), pos ) = Log(bop(Char.ord(n1),Char.ord(n2)),pos)
  | evalRelop ( bop, Log(n1,p1),     Log(n2,p2),     pos ) =
        let val i1 = if n1 then 1 else 0
            val i2 = if n2 then 1 else 0
        in  Log(bop(i1,i2),pos)
        end
  | evalRelop ( bop, e1, e2, pos ) = raise Error("Argument Types Do Not Match! Arg1: " ^
                                                 pp_exp 0 e1 ^ " Arg2: " ^ pp_exp 0 e2, pos)


  (*************************************************************)
  (*************************************************************)
  (*** Map-Reduce Fusion Optimization                        ***)
  (*************************************************************)
  (*************************************************************)

  fun redMapFus( exp, vtab ) = (false, exp)

  (*************************************************************)
  (*************************************************************)
  (*** Constant Folding (Propagation is Bonus)               ***)
  (*************************************************************)
  (*************************************************************)

  (* The code below is added as part of the exam *)

  fun ctFoldP ( exp, vtab ) = case exp of
      Fasto.Num(n,p) => (false, exp)
    | Fasto.Log(b,p) => (false, exp)
    | Fasto.CharLit(c,p) => (false, exp)
    | Fasto.StringLit(s,p) => (false, exp)
    | Fasto.ArrayLit(expl,t,p) =>
        let
          val (slist, explnew) = ListPair.unzip
                                 (map (fn e => ctFoldP(e,vtab)) expl)
          val s = List.exists(fn x => x = true) slist
        in
          (s, Fasto.ArrayLit(explnew, t, p))
        end
    | Fasto.Var(s,p) => (false, exp)
    | Fasto.Plus(e1, e2, p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
          val () = print(Fasto.pp_exp 1 e2new)
        in
          if
            (isCtVal e1new) andalso (isCtVal e2new)
          then
            (true,evalBinop(op+, e1new, e2new, p))
          else
            case (e1new, e2new) of
                ( Num(0,_), _         ) => (true,e2new)
              | ( _       , Num(0,_ ) ) => (true,e1new)
              | ( _       , _         ) => (s1 orelse s2,
                                            Fasto.Plus(e1new,e2new,p))
        end
    | Fasto.Minus(e1, e2, p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          if
            (isCtVal e1new) andalso (isCtVal e2new)
          then
            (true,evalBinop(op-, e1new, e2new, p))
          else
            case (e1new, e2new) of
                ( Num(0,_), _         ) => (true,Fasto.Negate(e2new,p))
              | ( _       , Num(0,_ ) ) => (true,e1new)
              | ( _       , _         ) => (s1 orelse s2,
                                            Fasto.Minus(e1new,e2new,p))
        end
    | Fasto.Equal(e1,e2,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          if
            (isCtVal e1new) andalso (isCtVal e2new)
          then
            (true,evalEq(e1new, e2new,p))
          else
            (s1 orelse s2, Fasto.Equal(e1new,e2new,p))
        end
    | Fasto.Less(e1,e2,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          if
            (isCtVal e1new) andalso (isCtVal e2new)
          then
            (true,evalRelop(op<,e1new,e2new,p))
          else
            (s1 orelse s2, Fasto.Equal(e1new,e2new,p))
        end
    | Fasto.If(e1,e2,e3,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
          val (s3, e3new) = ctFoldP( e3, vtab )
          val optimized = s1 orelse s2 orelse s3
          val newIf = Fasto.If(e1new,e2new,e3new,p)
        in
            (optimized, newIf)
        end
    | Fasto.Apply(str,expl,p) =>
        let
          val (slist, explnew) = ListPair.unzip (map (fn e => ctFoldP(e,vtab)) expl)
          val s1 = List.exists(fn x => x = true) slist
        in
          (s1, Fasto.Apply(str, explnew, p))
        end
    | Fasto.Let(Fasto.Dec(s,e1,p1), e2, p2) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          (s1 orelse s2, Fasto.Let(Fasto.Dec(s, e1new, p1), e2new, p2))
        end
    | Fasto.Index(str,e1,t,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
        in
          (s1, Fasto.Index(str, e1new,t,p))
        end
    | Fasto.Iota(e1,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
        in
          (s1, Fasto.Iota(e1new,p))
        end
    | Fasto.Map(str,e1,t1,t2,p)=>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
        in
          (s1, Fasto.Map(str,e1new,t1,t2,p))
        end
    | Fasto.Reduce(s, e1,e2,r,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          (s1, Fasto.Reduce(s, e1new,e2new,r,p))
        end
    | Fasto.Split(e1,e2,t,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          (s1, Fasto.Split(e1new,e2new,t,p))
        end
    | Fasto.Concat(e1,e2,t,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          (s1, Fasto.Concat(e1new,e2new,t,p))
        end
    | Fasto.Read(t,p) => (false, exp)
    | Fasto.Write(e1,t,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
        in
          (s1, Fasto.Write(e1new,t,p))
        end
    | Fasto.Times(e1,e2,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          if
            (isCtVal e1new) andalso (isCtVal e2new)
          then
            (true,evalBinop(op*, e1new, e2new, p))
          else
            case (e1new, e2new) of
                ( Num(0,_), _         ) => (true,Fasto.Num(0, p))
              | ( _       , Num(0,_ ) ) => (true,Fasto.Num(0, p))
              | ( Num(1,_), _         ) => (true,e2new)
              | ( _       , Num(1,_ ) ) => (true,e1new)
              | ( _       , _         ) => (s1 orelse s2,
                                            Fasto.Times(e1new,e2new,p))
        end
    | Fasto.Divide(e1,e2,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          if
            (isCtVal e1new) andalso (isCtVal e2new)
          then
            (true,evalBinop(op div, e1new, e2new, p))
          else
            case (e1new, e2new) of
                ( Num(0,_), _         ) => (true,Fasto.Num(0, p))
              | ( _       , Num(0,_ ) ) =>
                  raise Error ("Optimizer: Was ask to ctFold Divide exp with 0"^
                              "on righthandside, This dows not make sense", p)
              | ( _       , Num(1,_ ) ) => (true,e1new)
              | ( _       , _         ) => (s1 orelse s2,
                                            Fasto.Divide(e1new,e2new,p))
        end
    | Fasto.And(e1,e2,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          case (e1new, e2new) of
              ( Log(false,_), _            ) => (true, Log(false,p))
            | ( _           , Log(false,_) ) => (true, Log(false,p))
            | ( Log(true,_) , Log(true,_)  ) => (true, Log(true,p))
            | ( _           , _            ) => (s1 orelse s2, Fasto.And(e1new,e2new,p))
        end
    | Fasto.Band(e1,e2,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
          fun bitwiseand (n1,n2) =
            let
              val w1 = Word.fromInt(n1)
              val w2 = Word.fromInt(n2)
            in
              Word.toInt(Word.andb(w1,w2))
            end
        in
          if
            (isCtVal e1new) andalso (isCtVal e2new)
          then
            (true,evalBinop(bitwiseand, e1new, e2new, p))
          else
            case (e1new, e2new) of
                ( Num(0,_), _         ) => (true,Fasto.Num(0, p))
              | ( _       , Num(0,_ ) ) => (true,Fasto.Num(0, p))
              | ( _       , _         ) => (s1 orelse s2,
                                            Fasto.Times(e1new,e2new,p))
        end
    | Fasto.Or(e1,e2,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          case (e1new, e2new) of
              ( Log(true,_), _              ) => (true, Log(true,p))
            | ( _           , Log(true,_)   ) => (true, Log(true,p))
            | ( Log(false,_) , Log(false,_) ) => (true, Log(false,p))
            | ( _           , _             ) => (s1 orelse s2, Fasto.Or(e1new,e2new,p))
        end
    | Fasto.Not(e1,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
        in
            case e1new of
                Fasto.Log(b,_)  => (true, Fasto.Log(not b, p))
              | _               => (s1, Fasto.Not(e1new, p))
        end
    | Fasto.Negate(e1,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
        in
            case e1new of
                Fasto.Num(n,p1)  => (true, Fasto.Num(~n, p))
              | _               => (s1, Fasto.Negate(e1new, p))
        end
    | Fasto.ZipWith(str,e1,e2,t1,t2,t3,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          (s1 orelse s2, Fasto.ZipWith(str,e1new,e2new,t1,t2,t3,p))
        end
    | Fasto.Scan(str,e1,e2,t,p) =>
        let
          val (s1, e1new) = ctFoldP( e1, vtab )
          val (s2, e2new) = ctFoldP( e2, vtab )
        in
          (s1 orelse s2, Fasto.Scan(str,e1new,e2new,t,p))
        end
    (* Fallback, it can be discussed if it should be left out
     * to clarify programming mistakes when adding new AbSyn nodes. *)
    | _ => (false, exp)

  (*************************************************************)
  (*************************************************************)
  (*** Copy/Constant Propagation -- Implement                ***)
  (*************************************************************)
  (*************************************************************)
  fun copyProp( exp, vtab ) = case exp of
      Fasto.Num(n,p) => (false, exp)
    | Fasto.Log(b,p) => (false, exp)
    | Fasto.CharLit(c,p) => (false, exp)
    | Fasto.StringLit(s,p) => (false, exp)
    | Fasto.ArrayLit(expl,t,p) =>
        let
          val (slist, explnew) = ListPair.unzip (map (fn e => copyProp(e,vtab)) expl)
          val s = List.exists(fn x => x = true) slist
        in
          (s, Fasto.ArrayLit(explnew, t, p))
        end
    | Fasto.Var(id, p) => (case SymTab.lookup id vtab of
                           NONE   => (false, exp)
                        |  SOME e => (true, e))
    | Fasto.Plus (e1, e2, p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          ( s1 orelse s2, Fasto.Plus(e1new, e2new, p) )
        end
    | Fasto.Minus (e1, e2, p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          ( s1 orelse s2, Fasto.Minus(e1new, e2new, p) )
        end
    | Fasto.Equal(e1,e2,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
            (s1 orelse s2, Fasto.Equal(e1new,e2new,p))
        end
    | Fasto.Less(e1,e2,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
            (s1 orelse s2, Fasto.Less(e1new,e2new,p))
        end
    | Fasto.If(e1,e2,e3,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
          val (s3, e3new) = copyProp( e3, vtab )
          val optimized = s1 orelse s2 orelse s3
          val newIf = Fasto.If(e1new,e2new,e3new,p)
        in
            (optimized, newIf)
        end
    | Fasto.Apply(str,expl,p) =>
        let
          val (slist, explnew) = ListPair.unzip (map (fn e => copyProp(e,vtab)) expl)
          val s1 = List.exists(fn x => x = true) slist
        in
          (s1, Fasto.Apply(str, explnew, p))
        end
    | Fasto.Let(Fasto.Dec(id,e1,p1), e2, p2 ) =>
        let
          val (s1, e1new) = copyProp( e1, vtab );
          val vtabnew = if isCtVal(e1new) orelse isCpVar(e1new)
                        then SymTab.insert id e1new vtab
                        else vtab
        in
          let
            val (s2, e2new) = copyProp( e2, vtabnew )
          in
            if isCtVal(e1new) orelse isCpVar(e1new)
              then (true, e2new)
              else (s2 orelse s1, Fasto.Let(Fasto.Dec(id,e1new,p1), e2new, p2 ))
          end
        end
    | Fasto.Index(str,e1,t,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
        in
          (s1, Fasto.Index(str, e1new,t,p))
        end
    | Fasto.Iota(e1,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
        in
          (s1, Fasto.Iota(e1new,p))
        end
    | Fasto.Map(str,e1,t1,t2,p)=>
        let
          val (s1, e1new) = copyProp( e1, vtab )
        in
          (s1, Fasto.Map(str,e1new,t1,t2,p))
        end
    | Fasto.Reduce(s, e1,e2,r,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1, Fasto.Reduce(s, e1new,e2new,r,p))
        end
    | Fasto.Split(e1,e2,t,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1, Fasto.Split(e1new,e2new,t,p))
        end
    | Fasto.Concat(e1,e2,t,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1, Fasto.Concat(e1new,e2new,t,p))
        end
    | Fasto.Read(t,p) => (false, exp)
    | Fasto.Write(e1,t,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
        in
          (s1, Fasto.Write(e1new,t,p))
        end
    | Fasto.Times(e1,e2,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1 orelse s2, Fasto.Times(e1new,e2new,p))
        end
    | Fasto.Divide(e1,e2,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1 orelse s2, Fasto.Divide(e1new,e2new,p))
        end
    | Fasto.And(e1,e2,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1 orelse s2, Fasto.And(e1new,e2new,p))
        end
    | Fasto.Band(e1,e2,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1 orelse s2, Fasto.Times(e1new,e2new,p))
        end
    | Fasto.Or(e1,e2,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1 orelse s2, Fasto.Or(e1new,e2new,p))
        end
    | Fasto.Not(e1,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
        in
          (s1, Fasto.Not(e1new, p))
        end
    | Fasto.Negate(e1,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
        in
          (s1, Fasto.Negate(e1new, p))
        end
    | Fasto.ZipWith(str,e1,e2,t1,t2,t3,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1 orelse s2, Fasto.ZipWith(str,e1new,e2new,t1,t2,t3,p))
        end
    | Fasto.Scan(str,e1,e2,t,p) =>
        let
          val (s1, e1new) = copyProp( e1, vtab )
          val (s2, e2new) = copyProp( e2, vtab )
        in
          (s1 orelse s2, Fasto.Scan(str,e1new,e2new,t,p))
        end
    (* Fallback, it can be discussed if it should be left out
     * to clarify programming mistakes when adding new AbSyn nodes. *)
    | _ => (false, exp)

  (* -------------------------end-------------------------- *)

  (*************************************************************)
  (*************************************************************)
  (*** Dead-Code Elimination -- Implement                    ***)
  (*************************************************************)
  (*************************************************************)

  fun deadCode( exp, vtab ) = (false, exp)

  (*************************************************************)
  (*************************************************************)
  (*** Low-Level-Optimization Driver. To a fixed point:      ***)
  (***    1. Constant Folding and Propagation                ***)
  (***    2. Copy and Constant Propagation                   ***)
  (***    3. Dead-Code Elimination                           ***)
  (*************************************************************)
  (*************************************************************)
  fun lowLevOptAllFun(funlst : FunDec list) : FunDec list =
        let fun lowLevOptOneFP( onefun ) =
            let val (fid, rtp, fargs, body, pos) = onefun
                val (mod1, body1) = copyProp( body , SymTab.empty() )
                val (mod2, body2) = ctFoldP ( body1, SymTab.empty() )

                (* for dead-code elimination we need to feel the *)
                (* vtable with the formal args with counter 1    *)
                val symtab = map (fn(x,y)=>(x, ref 1)) fargs
                val (mod3, body3) = deadCode( body2, symtab )
                val () = print("Finished pass for function: "^fid^"\n")

                val success = mod1 orelse mod2 orelse mod3

            in if(success)
               then lowLevOptOneFP( (fid, rtp, fargs, body3, pos) )
               else let val () = print("FP ctFoldP, copyProp and deadCode done for "^fid^"!"^
                                       " Calling redMapFus and deadCode...\n")
                        val (mod4, body4) = redMapFus( body3, SymTab.empty() )
                        val (mod5, body5) = deadCode ( body4, symtab         )
                    in (fid, rtp, fargs, body5, pos)
                    end
            end

        in map lowLevOptOneFP funlst
        end


  (*************************************************************)
  (*************************************************************)
  (*** High-Level-Optimization Driver. To a fixed point:     ***)
  (***    1. Function Inlining                               ***)
  (***    2. Let Normalization                               ***)
  (***    3. Map Fusion                                      ***)
  (***    4. Dead-Code Elimination (perhaps when I have time)***)
  (*************************************************************)
  (*************************************************************)

  fun opt_fixpoint(funlst : FunDec list) : FunDec list =
        let val bodys = map (fn (fid, rtp, fargs, body, pdecl)
                                => let_norm_exp(body))
                            funlst

            val funnorm  = map (fn(new_body, (fid, rtp, fargs, body, p))
                                          => (fid,rtp,fargs,new_body,p))
                               (zip bodys funlst)

            val (succfus, funfus)  = mapfusion_driver( funnorm )

            (** Inline driver requires removal,
                and then addition of the special funs **)
            val funfus1 = rem_spec_funs(funfus)
            val (succinl, funsinl1) = inline_driver(funfus1, false)
            val funsinl = add_spec_funs(funsinl1)

            val funsCleaned = dead_fun_elim ( funsinl )

        in  if(succinl) then opt_fixpoint(funsCleaned) else funsCleaned
        end


  fun opt_pgm(funlst1 : FunDec list) : FunDec list =
          (* first add the special functions *)
      let val funlst = add_spec_funs(funlst1)

          val funscln = dead_fun_elim ( funlst )

          (* first make unique names for all function bodies *)
          fun rename_fun(fid, rtp, fargs, body, pdecl) =
            (fid, rtp, fargs, unique_rename( body, [] ), pdecl)

          val newfunlst = map (fn x=>rename_fun(x)) funscln
          val res1      = opt_fixpoint(newfunlst)

          val res2      = lowLevOptAllFun(res1)

          val res = rem_spec_funs(res2)
      in ( print(prettyPrint res); res )
      end

end
