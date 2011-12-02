(* Introduktion to programming - Weekly assignment 2
 *
 * Jonas Brunsgaard
 *)


(* 2i1 *)

(* The key n is a constant, therefore it is only nesessery to 
 * find n for one pair of coresponding ciphers in the input. 
 * This code subtract k from c. We can do this because we 
 * are only interrested in the difference between the last ciphers 
 * of the input. The difference mod 10 will return n.
 *)

fun brydcaesar (k,c) = (c - k) mod 10;

(* Testing the function "brydcaesar"*)
val test_2i1_1 = brydcaesar(99999999,22222222) =  3;


(* 2i2 *)

val brydcaesar_2i2 = brydcaesar (72926349,27471894);

(* Testing the function "brydcaesar", with input from assignment 2i2 *)
val test_2i2_0 = brydcaesar(72926349,27471894) =  5;



(* 2i3 *)

val brydcaesar_2i3 = brydcaesar (72926349,27461894);

(* Testing the function "brydcaesar" with input from assignment 2i3 *)
val test_2i3_0 = brydcaesar(72926349,27461894) =  5;

(* There is an error in the input. The code does not fail
 * because it's only the difference between the last ciphers in 
 * the input there are of relevace for the decrypt function.
 *)
