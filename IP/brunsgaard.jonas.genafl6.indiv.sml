
val sudukoFail =
    [[#"5", #"3", #"*", #"*", #"7", #"*", #"*", #"*", #"*"],
     [#"6", #"*", #"*", #"1", #"9", #"5", #"*", #"*", #"*"],
     [#"*", #"9", #"8", #"*", #"*", #"*", #"*", #"6", #"*"],
     [#"8"],
     [#"4", #"*", #"*", #"8", #"*", #"3", #"*", #"*", #"1"],
     [#"7", #"*", #"*", #"*", #"2", #"*", #"*", #"*", #"6"],
     [#"*", #"6", #"*", #"*", #"*", #"*", #"2", #"8", #"*"],
     [#"*", #"*", #"*", #"4", #"1", #"9", #"*", #"*", #"5"],
     [#"*", #"*", #"*", #"*", #"8", #"*", #"*", #"7", #"9"]]

val sudukoDone =
    [[#"5", #"3", #"4", #"6", #"7", #"8", #"9", #"1", #"2"],
     [#"6", #"7", #"2", #"1", #"9", #"5", #"3", #"4", #"8"],
     [#"1", #"9", #"8", #"3", #"4", #"2", #"5", #"6", #"7"],
     [#"8", #"5", #"9", #"7", #"6", #"1", #"4", #"2", #"3"],
     [#"4", #"2", #"6", #"8", #"5", #"3", #"7", #"9", #"1"],
     [#"7", #"1", #"3", #"9", #"2", #"4", #"8", #"5", #"6"],
     [#"9", #"6", #"1", #"5", #"3", #"7", #"2", #"8", #"4"],
     [#"2", #"8", #"7", #"4", #"1", #"9", #"6", #"3", #"5"],
     [#"3", #"4", #"5", #"2", #"8", #"6", #"1", #"7", #"9"]]

val sudukoNotDone =
    [[#"*", #"3", #"4", #"6", #"7", #"8", #"9", #"1", #"2"],
     [#"6", #"3", #"5", #"1", #"9", #"5", #"3", #"4", #"8"],
     [#"1", #"*", #"8", #"3", #"4", #"2", #"5", #"6", #"7"],
     [#"8", #"5", #"9", #"7", #"6", #"1", #"4", #"2", #"3"],
     [#"4", #"2", #"3", #"8", #"6", #"3", #"7", #"9", #"1"],
     [#"7", #"1", #"3", #"9", #"2", #"4", #"8", #"5", #"6"],
     [#"9", #"*", #"1", #"5", #"*", #"7", #"2", #"8", #"4"],
     [#"2", #"8", #"7", #"4", #"1", #"9", #"6", #"3", #"5"],
     [#"3", #"4", #"5", #"2", #"8", #"6", #"1", #"7", #"9"]]


(* 6i1 *)

local
  fun member (x,[]) = false
    | member (x,y::ys) = x=y orelse member (x,ys)
in
  fun delliste ys xs = List.all (fn z => z=true) (map (fn x => member(x,ys)) xs);  
end;

val test_6i1_1 = delliste [] [] = true;
val test_6i1_2 = delliste [1,2] [2,1] = true;
val test_6i1_3 = delliste [1,3] [1,2] = false;
val test_6i1_4 = delliste [1,2,3] [1,2] = true;
 

(* 6i2 *)

(* 
 * Her i genafleveringen har jeg fjernet længdetjekket som du bad om. 
 *)

fun tagliste n xs  =  map (fn ys => List.nth(ys,n)) xs
                     

val test_6i2_1 = tagliste 3 sudukoDone = 
                 [#"6", #"1", #"3", #"7", #"8", #"9", #"5", #"4", #"2"]

val test_6i2_2 = (tagliste 3 sudukoFail = 
               [#"5", #"6", #"1", #"8", #"4", #"7", #"9", #"2", #"3"]; false) 
               handle Subscript => true | _ => false;


(* 6i3 *)

(* Man kan tage funktionen delliste og køre den med en prædefineret liste  
 * der indeholder tegnene #"1"-#"9" 
 *
 * [#"1", #"2", #"3", #"4", #"5", #"6", #"7", #"8", #"9"]
 *
 * mod rækken i sudukoen, som man gerne vil tjekke.
 *
 * Hvis funktionen returnerer "true" ved man at alle tegnene #"1"-#"9" er 
 * tilstede.
 *
 * Hvis og kun hvis at listen, der repræsentere rækken, har 9 elementer kan man
 * med sikkerhed sige at tegnene #"1"-#"9" kun forekommer een gang.
 *
 * Tagliste kan lave en liste der repræsentere tegnene i en kolonne. 
 * Listen kan herefter tjekkes med delliste som beskrevet ovenfor.
 *)


(* 6i4 *)
val testliste = [#"1", #"2", #"3", #"4", #"5", #"6", #"7", #"8", #"9"]

local
  val testr = List.all (fn z => z=true) o  map (fn x => delliste testliste x);

  fun rTils xs = let 
           fun rTilsLoop n = if n<=8 then tagliste n xs :: rTilsLoop (n+1) else [];
               in rTilsLoop 0
               end;

  val tests = (List.all (fn z => z=true) o 
               map (fn x => delliste testliste x) o rTils);


in  
  fun rscheck xs = testr xs andalso tests xs;
end;


val test6i4_1 = rscheck sudukoDone = true;
val test6i4_2 = rscheck sudukoNotDone = false;


(* 6i5 *)
local
  val testraekker = List.all (fn z => z=true) o 
                    map (fn x => delliste testliste x);

  fun rTils xs = let 
           fun rTilsLoop n = if n<=8 
                             then tagliste n xs :: rTilsLoop (n+1) 
                             else [];
                 in rTilsLoop 0
                 end;

  val testsaejler = List.all (fn z => z=true) o 
                    map (fn x => delliste testliste x) o rTils;

  fun regliste xs n = 
   let 
    val raekke = n div 3 * 3 
    val kolonne = n mod 3 * 3 
    val mellemres = map (fn x=> List.take ((List.drop (x, kolonne)), 3)) 
      (List.take ((List.drop (xs, raekke)), 3)) 
   in
    foldl op@ [] mellemres
   end;


  fun regionerTilListe xs = 
      let 
        fun regionerTilListeLoop n = 
               if n<=8 
               then regliste xs n :: regionerTilListeLoop (n+1) 
               else [];
      in 
        regionerTilListeLoop 0
      end;

  val testRegioner = 
      List.all (fn z => z=true) o map (fn x => delliste testliste x) o regionerTilListe;
in
  fun sudocheck xs = 
      testraekker xs andalso testsaejler xs andalso testRegioner xs;
end;

val test6i5_1 = sudocheck sudukoDone = true;
val test6i5_2 = sudocheck sudukoNotDone = false;
