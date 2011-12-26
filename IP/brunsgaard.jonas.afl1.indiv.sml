(* -- 1i1 -- *)

(* Function taken from HR page 7, section 1.5*)
fun pow (x, 0) = 1.0
  | pow (x, n) = x * pow(x, n-1);

fun prob (p, n, k) = pow(p, k) * pow(1.0-p, n-k);

(* The value "1953125/2176782336" is the excact result of the function prob, 
when p=1/6, n=12 and k = 3, the value is calcualted with a TI-89.*) 

val test_1i1 = prob(1.0/6.0, 12, 3) = 1953125.0/2176782336.0;

val why_false_test_1i1 = prob(1.0/6.0, 12, 3) - (1953125.0/2176782336.0);

(* 
 * Floating-point representation of real numbers implies that the approach to testing given by 
 * "Vejledning i afprøvning" do not suffice. (At least it's my guess) Is that right? And if yes, 
 * how do we test functions which output values of the type real.
 *
 * The value "why_false_test_1i1" is so tiny (near zero), that I will deduce that the function 
 * is working correct
 *)


(* -- 1i2 -- *)

(* Function taken from HR page 7, subsesction 1.4.3*)
fun fact 0 = 1
  | fact n = n * fact(n-1);

(* Function taken from group paper 1, subassignment 1g2 *)
fun binomk (n, k) = fact n div (fact(k)*fact(n-k));

fun binompr (n, k, p) =  real(binomk(n, k)) * prob(p, n, k);

val test_1i2_1 = binompr(12, 3, 1.0/6.0) - (107421875.0/544195584.0);

(* The value "test_1i2" is so tiny, that i will deduce that the function 
is working correct *)

(* Alternative testing*)
val test_1i2_2 = 6.0 * binompr (1, 1, 1.0/6.0) = 1.0;


(* -- 1i3 -- *)

val probability_1i3 = binompr (12, 3, 1.0/6.0);
