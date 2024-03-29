(*

Fasto er et funktionelt array-sprog til oversættelse, F-A-S-T-O.
Fasto er også et spansk ord, der betyder "pomp" eller "pragt".
Derfor skal vi programmere en "pragtfuld" oversætter for Fasto.

*)
structure  Fasto =
struct

  (* types for abstract syntax for Fasto *)

  type pos = int * int  (* position: (line, column) *)

  datatype Type
    = Int of pos
    | Bool of pos
    | Char of pos
    | Array of Type * pos
    | UNKNOWN (* filled by type checker *)

  and Exp
    = Num of int * pos
    | Log of bool * pos
    | CharLit of char * pos
    | StringLit of string * pos            (* e.g., "Hello World!\n"       *)
    | ArrayLit of Exp list * Type * pos    (* e.g., { {1+x, 3}, {2, {1+4}} *)
                                             (* Type is the array's element type *)
    | Var of string * pos                  (* e.g., x}    *)
    | Plus of Exp * Exp * pos              (* e.g., x + 3 *)
    | Minus of Exp * Exp * pos             (* e.g., x - 3 *)
    | Equal of Exp * Exp * pos             (* e.g., x = 3 *)
    | Less of Exp * Exp * pos
    | If of Exp * Exp * Exp * pos
    | Apply of string * Exp list * pos     (* e.g., f(1, 3+x) *)
    | Let of Dec * Exp * pos               (* e.g., let x = 1 + y in x + y *)
    (* arrays *)
    | Index of string * Exp * Type * pos         (* arr[3]            *)
    | Iota of Exp * pos                          (* iota(n)           *)
    | Map of string * Exp * Type * Type * pos    (* map(f, lst)       *)
    | Reduce of string * Exp * Exp * Type * pos  (* reduce(f, 0, lst) *)
    | Replicate of Exp * Exp * Type * pos        (* replicate(n, 0)   *)
    | Read of Type * pos                         (* e.g., read(int) *)
    | Write of Exp * Type * pos                  (* e.g., write(map(f, replicate(3,x))) *)
                                                   (* Type is the type of the to-be-written element *)
(* extension in G exercise *)
    | Times of Exp * Exp * pos
    | Divide of Exp * Exp * pos
    | And of Exp * Exp * pos
    | Or of Exp * Exp * pos
    | Not of Exp * pos
    | Negate of Exp * pos

    (* second-order-array combinators *)
    | ZipWith of string * Exp * Exp * Type * Type * Type * pos  (* zipWith(plus, {1,2,3}, {4,5,6}) == {5, 7, 9} *)
    | Scan of string * Exp * Exp * Type * pos                   (* scan plus 0 { 1, 2, 3 } = { 0, 1, 3, 6 } *)

    (* code added for exam - msn378  *)

    (* Split: arguments are: splitindex(Num), array(Array), type of elements
     * in Array elements(Fasto.Unknown until typechecking) , pos *)
    | Split of Exp * Exp * Type * pos

    (* Concat: arguments are: array1(Array), array2(Array), type of elements
     * in Arrays elements(Fasto.Unknown until typechecking) , pos *)
    | Concat of Exp * Exp * Type *  pos

    (* Band (bitwise and): arguments are: Num, Num, pos *)
    | Band of Exp * Exp * pos

    (* -------- end ------- *)

  and Dec = Dec of string * Exp * pos


  type Binding = (string * Type)

  type FunDec = (string * Type * Binding list * Exp * pos)

  type Prog = FunDec list

(****************************************************)
(********** Pretty-Printing Functionality ***********)
(****************************************************)

  fun makeDepth 0 = ""
    | makeDepth n = "  " ^ (makeDepth (n-1))

  (* pretty printing an expression *)
  fun pp_exp d (Num   (n,  pos))     = Int.toString n
    | pp_exp d (Log   (b,  pos))     = Bool.toString b
    | pp_exp d (CharLit (c,  pos))   = "'" ^ Char.toCString c ^ "'"
    | pp_exp d (StringLit (s,  pos)) = "\"" ^ String.toCString s ^ "\""
    | pp_exp d (ArrayLit (lst, t, pos) ) =
        ( case lst of
            [ ]    => " { } "
          | (a::l) => " { "^pp_exp d a^concat (map (fn x => ", "^pp_exp d x) l) ^ " } "
        )
    | pp_exp d (Var   (id, pos))     = "" ^id
    | pp_exp d (Plus  (e1, e2, pos))    = " ( " ^ pp_exp d e1 ^ " + " ^ pp_exp d e2 ^ " ) "
    | pp_exp d (Minus (e1, e2, pos))    = " ( " ^ pp_exp d e1 ^ " - " ^ pp_exp d e2 ^ " ) "

    | pp_exp d (Times (e1,e2,_)) = " ( " ^ pp_exp d e1 ^ " * " ^ pp_exp d e2 ^ " ) "
    | pp_exp d (Divide (e1,e2,_))= " ( " ^ pp_exp d e1 ^ " / " ^ pp_exp d e2 ^ " ) "
    | pp_exp d (And (e1,e2,_))   = " ( " ^ pp_exp d e1 ^ " & " ^ pp_exp d e2 ^ " ) "
    | pp_exp d (Band (e1,e2,_))   = " ( " ^ pp_exp d e1 ^ " band " ^ pp_exp d e2 ^ " ) "
    | pp_exp d (Or (e1,e2,_))    = " ( " ^ pp_exp d e1 ^ " | " ^ pp_exp d e2 ^ " ) "
    | pp_exp d (Not (e,_))       = " ( " ^ "not " ^ pp_exp d e ^ " ) "
    | pp_exp d (Negate (e,_))    = " ( " ^ "~ " ^ pp_exp d e ^   " ) "


    | pp_exp d (Equal (e1, e2, pos))    = " ( " ^ pp_exp d e1 ^ " = " ^ pp_exp d e2 ^ " ) "
    | pp_exp d (Less  (e1, e2, pos))    = " ( " ^ pp_exp d e1 ^ " < " ^ pp_exp d e2 ^ " ) "
    | pp_exp d (If    (e1, e2, e3, pos))  = "\n" ^
                makeDepth (d+1) ^ "if( " ^ pp_exp d e1 ^ " )\n" ^
		makeDepth (d+2) ^ "then " ^ pp_exp (d+2) e2 ^ "\n" ^
                makeDepth (d+2) ^ "else " ^ pp_exp (d+2) e3 ^ "\n" ^
                makeDepth d
    | pp_exp d (Apply (id, args, pos))    =
                ( case args of
                    []     => id ^ "() "
                  | (a::l) => id ^ "( " ^ pp_exp d a ^ concat (map (fn x => ", "^pp_exp d x) l) ^ " ) "
                )
    | pp_exp d (Let   (Dec(id, e1, pos1), e2, pos2)) =
                "\n"^makeDepth(d+1)^"let " ^ id ^ " = " ^ pp_exp (d+2) e1 ^
                " in  " ^ pp_exp (d+2) e2
    | pp_exp d (Index (id, e, t, pos))       =
                id ^ "[ " ^ pp_exp d e ^ " ]"
    (* Array Constructs *)
    | pp_exp d (Iota (e, pos))         = "iota ( " ^ pp_exp d e ^ " ) "
    | pp_exp d (Map(id, e, _,_, pos))    = "map ( " ^ id ^ ", " ^ pp_exp d e ^ " ) "
    | pp_exp d (ZipWith(id, e1, e2, _,_,_, pos))    = "zipWith ( " ^ id ^ ", " ^ pp_exp d e1 ^ pp_exp d e2 ^ " ) "
    | pp_exp d (Reduce(id, el, lst, t, pos)) = "reduce ( "^id^", "^pp_exp d el^", "^pp_exp d lst^" ) "
    | pp_exp d (Scan  (id, el, lst, t, pos)) = "scan ( "^id^", "^pp_exp d el^", "^pp_exp d lst^" ) "
    | pp_exp d (Replicate(e, el, t, pos)) = "replicate ( "^pp_exp d e^", "^pp_exp d el^" ) "
    | pp_exp d (Read (t,p)) = "read(" ^pp_type t ^") "
    | pp_exp d (Write (e,t,p)) = "write(" ^pp_exp d e ^") "

    | pp_exp d (Split(exp1, exp2, typ, pos))    = "Split ( " ^ pp_exp d exp1 ^ ", " ^ pp_exp d exp1 ^ ", " ^ pp_type typ ^ " ) "
    | pp_exp d (Concat(exp1, exp2, typ, pos))   = "Concat ( " ^ pp_exp d exp1 ^ ", " ^ pp_exp d exp1 ^ ", " ^ pp_type typ ^ " ) "

  (* pretty printing a type *)
  and pp_type (Int  pos) = "int "
    | pp_type (Bool pos) = "bool "
    | pp_type (Char pos) = "char "
    | pp_type (Array (tp, pos)) = "[ " ^ pp_type tp ^ " ] "
    | pp_type UNKNOWN = "UNKNOWN "

  (* pretty printing a function declaration *)
  fun pp_fun d (id, ret_tp, args, body, pos) =
    let (* pretty printing a list of bindings separated by commas *)
        fun pp_bd  (id : string, tp : Type) = pp_type tp ^ " " ^ id
        fun pp_cbd (bd : (string * Type)) = ", " ^ pp_bd bd
        fun pp_bindings []      = ""
          | pp_bindings [bd]    = pp_bd bd
          | pp_bindings (bd::l) = pp_bd bd ^ concat (map pp_cbd l)

    in "\n\nfun " ^ pp_type ret_tp ^ id ^
       "( " ^ pp_bindings args ^ ") = \n" ^
       makeDepth (d+1) ^ pp_exp (d+1) body
    end

  (* pretty printing a PROGRAM *)
  fun prettyPrint (p : Prog) = concat (map (pp_fun 0) p) ^ "\n"

end
