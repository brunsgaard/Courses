(* 3i1 *)

fun repchar (c, 0) = []
  | repchar (c, m) = c::repchar(c, m-1);  

val test_3i1_1 = repchar (#"a", 3) = [#"a", #"a", #"a"];
val test_3i1_2 = repchar (#"a", 0) = [];


(* 3i2 *) 

fun combine [] = []
  | combine [x] = raise Fail ("inputlist must hold an even number of elements")
  | combine (x1::x2::xs) = (x1,x2)::combine xs;  

val test_3i2_1 = combine [] = [];
val test_3i2_2 = (combine ([1, 1, 2, 2, 3]); false) handle Fail _ => true |_ =>false;
val test_3i2_3 = combine ([1, 1, 2, 2, 3, 3]) = [(1, 1), (2, 2), (3, 3)];


(* 3i3 *)

local
(* combine2pars takes a char list and converts it into a parlist, all 
 * even-numbered elements in the inputlist is converted to the type integer.
 *
 * combine2pars = fn : char list -> (char * int) list
 *)

fun combine2pars [] =[]
  | combine2pars [x]= raise Fail ("inputlist must hold an even number of elements")
  | combine2pars (x1::x2::xs) = (x1,ord(x2)-48)::combine2pars xs

(* parlistToCharlist takes a par (char * int) in a parlist and returns a charlist. 
 * The function append the returns for all the pars in the parlist.
 * 
 * parlistToCharlist = fn : ('a * int) list -> 'a list
 *)

fun parlistToCharlist  [] = []
  | parlistToCharlist ((x,y)::xs) = repchar(x,y) @ parlistToCharlist xs

in

fun rldecompress s = implode(repchar2(combine2pars(explode(s))))

end;

val test_3i3_1 = rldecompress ("k2h6l2s8j4h6s7") = 
"kkhhhhhhllssssssssjjjjhhhhhhsssssss";
