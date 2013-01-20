#!/bin/sh
#
# Compile the compiler and run the test suite using the interpreter.
#
# Test programs with no corresponding .in files are expected to fail
# at compile-time.  These are not run: use testg.sh to test them.  The
# error message is not controlled.  This script should be run from the
# directory containing the compiler sources, but can be run from
# anywhere, as long as you change RESDIR and COMPILER.
#
# Written by Troels Henriksen  <athas@sigkill.dk>.

set -e # Die on first error.

# RESDIR is the path at which test programs can be found.
RESDIR=../DATA

# COMPILER is the command to run the compiler
COMPILER=../BIN/FastoC

sh compile.sh

for FO in $RESDIR/*fo; do
    FO=$(basename $FO)
    echo Testing $FO:
    PROG=$(echo $FO|sed 's/.fo$//')
    INPUT=$(echo $FO|sed 's/fo$/in/')
    OUTPUT=$(echo $FO|sed 's/fo$/out/')
    ERROUT=$(echo $FO|sed 's/fo$/err/')
    ASM=$(echo $FO|sed 's/fo$/asm/')
    TESTOUT=$RESDIR/$OUTPUT-testresult
    CORRECTOUT=$(mktemp)
    if [ -f $RESDIR/$INPUT ]; then
        $COMPILER -i $RESDIR/$PROG < $RESDIR/$INPUT \
            | awk '/^RESULT/{out=0}out{print}/^Input/{out=1}' | tr -d '[:space:]' > $TESTOUT # Remove all whitespace before testing.  Ugh.
        if [ -f $RESDIR/$OUTPUT ]; then
            tail -n +2 $RESDIR/$OUTPUT | tr -d '[:space:]' > $CORRECTOUT
            if ! cmp -s $CORRECTOUT $TESTOUT; then
                echo Output for $PROG does not match expected output.
                echo Compare $TESTOUT and $RESDIR/$OUTPUT.
            else rm -f $TESTOUT
            fi
        else
            rm -f $TESTOUT
        fi
    fi
    rm -f $CORRECTOUT
done
