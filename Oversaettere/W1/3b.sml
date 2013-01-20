local open Obj Lexing in


    open Lexing;


(*
 * Exception section
 *)

    (* Exception stuff *)
    exception LexicalError of string * int;

    fun lexerError lexbuf s =
        raise LexicalError (s, getLexemeStart lexbuf);


(*
 * Datatype section (Normaly not part of the lexer)
 *)

    (* Token types, normaly part of the parser *)
    datatype TokenType = OCLOCK of int
                       | HALFPAST of int
                       | QUARTERPAST of int
                       | TO of int * int
                       | EOF;


(*
 * Decode section
 *)

    fun decodeTo lexbuf =
        let
          val s = getLexeme lexbuf
          val xs = String.tokens (fn x => x = #" ") s
          val min = List.nth(xs, 0)
          val hour = List.nth(xs, 2)
        in
          case (Int.fromString min, Int.fromString hour) of
            (SOME m, SOME h) => TO (m, h)
          | _                => lexerError lexbuf (
                                min ^ " or " ^ hour ^ " are not an int.")
        end;

    fun decodeQaurterPast lexbuf =
        let
          val s = getLexeme lexbuf
          val xs = String.tokens (fn x => x = #" ") s;
          val hour = List.nth(xs, 2)
        in
          case Int.fromString hour of
            SOME h => QUARTERPAST h
          | NONE   => lexerError lexbuf ( hour ^ ": not an int.")
        end;

    fun decodeHalfPast lexbuf =
        let
          val s = getLexeme lexbuf
          val xs = String.tokens (fn x => x = #" ") s;
          val hour = List.nth(xs, 2)
        in
          case Int.fromString hour of
            SOME h => HALFPAST h
          | NONE   => lexerError lexbuf ( hour ^ ": not an int.")
        end;

    fun decodeOclock lexbuf =
        let
          val s = getLexeme lexbuf
        in
          case Int.fromString s of
            SOME h => OCLOCK h
          | NONE   => lexerError lexbuf ( s ^ ": does not start with an int")
        end;


(*
 * Scanner section
 *)

    (* Function for scanning a string, 'lex' will become the Token function *)
    fun Scan lex s =
        let
          val buf = createLexerString s
          fun repeat lex b =
              let
                val res = lex b
              in
                res :: (if
                          res = EOF
                        then
                          []
                        else
                          repeat lex b)
              end
        in
          repeat lex buf
        end
        handle LexicalError (msg, pos)
           => (TextIO.output (TextIO.stdErr, msg ^ makestring pos ^"\n");[]);


fun action_6 lexbuf = (
 lexerError lexbuf ("Lexical error at input `"^getLexeme lexbuf^ "`") )
and action_5 lexbuf = (
 EOF )
and action_4 lexbuf = (
 Token lexbuf )
and action_3 lexbuf = (
 decodeOclock lexbuf )
and action_2 lexbuf = (
 decodeHalfPast lexbuf)
and action_1 lexbuf = (
 decodeQaurterPast lexbuf)
and action_0 lexbuf = (
 decodeTo lexbuf )
and state_0 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"\n" => action_4 lexbuf
 |  #"\t" => action_4 lexbuf
 |  #"\r" => action_4 lexbuf
 |  #" " => action_4 lexbuf
 |  #"," => action_4 lexbuf
 |  #"9" => state_7 lexbuf
 |  #"8" => state_7 lexbuf
 |  #"7" => state_7 lexbuf
 |  #"6" => state_7 lexbuf
 |  #"5" => state_6 lexbuf
 |  #"4" => state_6 lexbuf
 |  #"3" => state_6 lexbuf
 |  #"2" => state_6 lexbuf
 |  #"q" => state_9 lexbuf
 |  #"h" => state_8 lexbuf
 |  #"1" => state_5 lexbuf
 |  #"0" => state_4 lexbuf
 |  #"\^@" => action_5 lexbuf
 |  _ => action_6 lexbuf
 end)
and state_4 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_6);
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_49 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_5 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_6);
 let val currChar = getNextChar lexbuf in
 if currChar >= #"3" andalso currChar <= #"9" then  state_48 lexbuf
 else case currChar of
    #"2" => state_50 lexbuf
 |  #"1" => state_50 lexbuf
 |  #"0" => state_50 lexbuf
 |  #" " => state_35 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_6 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_6);
 let val currChar = getNextChar lexbuf in
 if currChar >= #"0" andalso currChar <= #"9" then  state_48 lexbuf
 else case currChar of
    #" " => state_35 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_7 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_6);
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_35 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_8 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_6);
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"a" => state_24 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_9 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_6);
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"u" => state_10 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_10 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"a" => state_11 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_11 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"r" => state_12 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_12 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"t" => state_13 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_13 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"e" => state_14 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_14 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"r" => state_15 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_15 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_16 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_16 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"p" => state_17 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_17 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"a" => state_18 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_18 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"s" => state_19 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_19 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"t" => state_20 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_20 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_21 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_21 lexbuf = (
 let val currChar = getNextChar lexbuf in
 if currChar >= #"2" andalso currChar <= #"9" then  action_1 lexbuf
 else case currChar of
    #"1" => state_22 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_22 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_1);
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"2" => action_1 lexbuf
 |  #"1" => action_1 lexbuf
 |  #"0" => action_1 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_24 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"l" => state_25 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_25 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"f" => state_26 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_26 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_27 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_27 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"p" => state_28 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_28 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"a" => state_29 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_29 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"s" => state_30 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_30 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"t" => state_31 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_31 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_32 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_32 lexbuf = (
 let val currChar = getNextChar lexbuf in
 if currChar >= #"2" andalso currChar <= #"9" then  action_2 lexbuf
 else case currChar of
    #"1" => state_33 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_33 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_2);
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"2" => action_2 lexbuf
 |  #"1" => action_2 lexbuf
 |  #"0" => action_2 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_35 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"t" => state_37 lexbuf
 |  #"o" => state_36 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_36 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"'" => state_42 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_37 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"o" => state_38 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_38 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_39 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_39 lexbuf = (
 let val currChar = getNextChar lexbuf in
 if currChar >= #"2" andalso currChar <= #"9" then  action_0 lexbuf
 else case currChar of
    #"1" => state_40 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_40 lexbuf = (
 setLexLastPos lexbuf (getLexCurrPos lexbuf);
 setLexLastAction lexbuf (magic action_0);
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"2" => action_0 lexbuf
 |  #"1" => action_0 lexbuf
 |  #"0" => action_0 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_42 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"c" => state_43 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_43 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"l" => state_44 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_44 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"o" => state_45 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_45 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"c" => state_46 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_46 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"k" => action_3 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_48 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_49 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_49 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #"t" => state_37 lexbuf
 |  _ => backtrack lexbuf
 end)
and state_50 lexbuf = (
 let val currChar = getNextChar lexbuf in
 case currChar of
    #" " => state_35 lexbuf
 |  _ => backtrack lexbuf
 end)
and Token lexbuf =
  (setLexLastAction lexbuf (magic dummyAction);
   setLexStartPos lexbuf (getLexCurrPos lexbuf);
   state_0 lexbuf)

(* The following checks type consistency of actions *)
val _ = fn _ => [action_6, action_5, action_4, action_3, action_2, action_1, action_0];

end
