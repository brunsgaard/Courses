(* 4i1 *)
val suffixsen = map (fn x => x^"sen");

val test_4i1_1 = suffixsen ["Troels","kan","godt","lide","tests"] = 
["Troelssen","kansen","godtsen","lidesen","testssen"];
val test_4i1_2 = suffixsen [] = [];

fun suffixgen s xs = map (fn x => x^s) xs;

val test_4i1_3 = suffixgen ".com" ["pornhub","xvideos","redtube"] = 
["pornhub.com","xvideos.com","redtube.com"];
val test_4i1_4 = suffixgen ".com" [] = [];
val test_4i1_5 = suffixgen "" ["emptysuffix"]= ["emptysuffix"]

(* 4i2 *)
fun mellemsaml [] = ""
  | mellemsaml [t] = t
  | mellemsaml (t::ts) = foldl (fn (x, y) => y ^ " " ^ x ) t ts; 

val test_4i2_1 = mellemsaml [] = "";
val test_4i2_2 = mellemsaml ["t"] = "t";
val test_4i2_3 = mellemsaml ["pornhub","xvideos","redtube"] = 
"pornhub xvideos redtube";

(* Forskellen er at den folder fra højre, hvilket vi ikke kan 
lide, da det er langsommere end and folde fra venstre. 
Derudover vil der være det "buggy" mellemrum som jeg fik 
elimineret i min egen funktion.*) 

(* 4i3 *)
local
  fun klh (x,(korte,lange))  = if size x < 7 
                               then (korte+1,lange) 
                               else (korte,lange+1);
in
  fun kortelange xs = foldl klh (0,0) xs;
end;

val test_4i3_1 = kortelange [] = (0,0);
val test_4i3_1 = kortelange ["en", "ttoooooo", "treeee", "4"] = (3, 1);
