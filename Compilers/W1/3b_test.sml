load "Lexing";
load "Obj";
use "3b.sml";
print("\n\n SOME TESTING VALUES BELOW\n\n");
val testString = "23 to 12, 59 to 11, quarter past 1, half past 12, 11 o'clock";
val result = Scan Token testString;

val testString_err = "err";

val result = Scan Token testString_err
