{
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

}

let hours = [`1`-`9`] | "10" | "11" | "12"
let minutes = [`0`-`9`] | [`1`-`5`][`0`-`9`] 

rule Token = parse
          minutes " to " hours { decodeTo lexbuf }
        | "quarter past " hours { decodeQaurterPast lexbuf}
        | "half past " hours { decodeHalfPast lexbuf}
        | hours " o'clock" { decodeOclock lexbuf }
        | [` ` `\t` `\n` `\r` `,`] { Token lexbuf }
        | eof { EOF }
        | _ { lexerError lexbuf ("Lexical error at input `"^getLexeme lexbuf^ "`") }
;
