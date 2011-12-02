(* datatyper fra gruppeopgaven og timerne, som skal bruges i 
   besvarelsen af opgaven *)

datatype aarstal = Aar of int | UkendtAar | Irrelevant;
datatype navn = Navn of string | UkendtNavn;
datatype koen  = Mand | Kvinde | UkendtKoen;
datatype person = Person of (navn * koen * aarstal * aarstal);
datatype aneTrae = Leaf | Node of (person * aneTrae * aneTrae);
datatype foraeldre = FAR | MOR;

local
  val person1 = Person (Navn "Jakob", Mand, Aar 2000, Aar 2002);
  val person2 = Person (Navn "Jan", Mand, Aar 1990 , Aar 2008);
  val person3 = Person (Navn "Orla", Mand, Aar 1980, Aar 2009);
  val person4 = Person (Navn "Adam", Mand, Aar 1979, Aar 2004);
  val person5 = Person (Navn "Eva", Kvinde, Aar 1970, Irrelevant);
  val person6 = Person (Navn "Inge", Kvinde, Aar 1980, Irrelevant);
  val person7 = Person (Navn "Jette", Kvinde, Aar 1990, Irrelevant);
  val person8 = Person (Navn "Gunnar", Kvinde, Aar 1980, Irrelevant);
  val person9 = Person (Navn "Gyda", Kvinde, Aar 1980, Irrelevant);
  val person10 = Person (Navn "Gyda", Kvinde, Aar 1980, Irrelevant);
in
  val busken =
      (Node (person1,
          (Node (person2,
              (Node (person3, 
                  (Node (person4, Leaf, Leaf)), 
                  (Node (person5, Leaf, Leaf)))), 
              (Node (person6, Leaf, Leaf)))),
          (Node (person7, 
              (Node (person8, Leaf, Leaf)), 
              (Node (person9, Leaf, Leaf)))))); 

end;

(*
* Selve opgaven starter nedenfor
*)


(* 5i1 *)
fun samePerson (p1,p2) = p1 = p2;

val PersonTest1 = Person (Navn "Faetter BR", Mand, Aar 2000, Aar 2002);
val PersonTest2 = Person (Navn "Faetter BR", Kvinde, Aar 2000, Aar 2002);
val PersonTest3 = Person (Navn "Faetter BR", Mand, Aar 2000, Aar 2002);
 

val test_5i1_1 = not (samePerson(PersonTest1,PersonTest2));

val test_5i1_2 = samePerson(PersonTest1,PersonTest3);

 
(* 5i2 *)

fun fjern (Node(p,f,m),x::xs) = 
        (case x of MOR => Node(p,f,fjern (m,xs))
                 | Far => Node(p,fjern (f,xs),m))
  | fjern _ = Leaf;

val test_5i2_1 = fjern (busken, [FAR]) =  
   Node(Person(Navn "Jakob", Mand, Aar 2000, Aar 2002), Leaf,
         Node(Person(Navn "Jette", Kvinde, Aar 1990, Irrelevant),
              Node(Person(Navn "Gunnar", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf),
              Node(Person(Navn "Gyda", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf)));

val test_5i2_2 = fjern (busken, [MOR,MOR,MOR,MOR,MOR]) = 
   Node(Person(Navn "Jakob", Mand, Aar 2000, Aar 2002),
         Node(Person(Navn "Jan", Mand, Aar 1990, Aar 2008),
              Node(Person(Navn "Orla", Mand, Aar 1980, Aar 2009),
                   Node(Person(Navn "Adam", Mand, Aar 1979, Aar 2004), Leaf,
                        Leaf),
                   Node(Person(Navn "Eva", Kvinde, Aar 1970, Irrelevant), Leaf,
                        Leaf)),
              Node(Person(Navn "Inge", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf)),
         Node(Person(Navn "Jette", Kvinde, Aar 1990, Irrelevant),
              Node(Person(Navn "Gunnar", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf),
              Node(Person(Navn "Gyda", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf)));

(* 5i3 *)

fun indsaet 
        (Node(p,f,m),x::xs,personToInsert) = 
        (case x of MOR => Node(p,f,indsaet(m,xs,personToInsert))
                 | Far => Node(p,indsaet(f,xs,personToInsert),m))
  | indsaet (Node(_,f,m),_,personToInsert) = Node(personToInsert,f,m)
  | indsaet (Leaf,_,personToInsert) = Node(personToInsert,Leaf,Leaf);


val test_person = Person (Navn "Alfred", Mand, Aar 1980, Irrelevant);

val test_5i3_1 = indsaet (busken, [FAR,FAR,FAR,FAR], test_person ) =
    Node(Person(Navn "Jakob", Mand, Aar 2000, Aar 2002),
         Node(Person(Navn "Jan", Mand, Aar 1990, Aar 2008),
              Node(Person(Navn "Orla", Mand, Aar 1980, Aar 2009),
                   Node(Person(Navn "Adam", Mand, Aar 1979, Aar 2004),
                        Node(Person(Navn "Alfred", Mand, Aar 1980, Irrelevant),
                             Leaf, Leaf), Leaf),
                   Node(Person(Navn "Eva", Kvinde, Aar 1970, Irrelevant), Leaf,
                        Leaf)),
              Node(Person(Navn "Inge", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf)),
         Node(Person(Navn "Jette", Kvinde, Aar 1990, Irrelevant),
              Node(Person(Navn "Gunnar", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf),
              Node(Person(Navn "Gyda", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf)));

val test_5i3_2 = indsaet (busken, [MOR], test_person ) =
    Node(Person(Navn "Jakob", Mand, Aar 2000, Aar 2002),
         Node(Person(Navn "Jan", Mand, Aar 1990, Aar 2008),
              Node(Person(Navn "Orla", Mand, Aar 1980, Aar 2009),
                   Node(Person(Navn "Adam", Mand, Aar 1979, Aar 2004), Leaf,
                        Leaf),
                   Node(Person(Navn "Eva", Kvinde, Aar 1970, Irrelevant), Leaf,
                        Leaf)),
              Node(Person(Navn "Inge", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf)),
         Node(Person(Navn "Alfred", Mand, Aar 1980, Irrelevant),
              Node(Person(Navn "Gunnar", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf),
              Node(Person(Navn "Gyda", Kvinde, Aar 1980, Irrelevant), Leaf,
                   Leaf)));

(* 5i4 *)

val test_indsaet = indsaet (busken, [MOR], test_person );
val test_fjern = fjern (busken, [FAR]);

