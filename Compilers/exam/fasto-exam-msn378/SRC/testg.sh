#!/bin/sh
#
# Compile the compiler and run the test suite.
#
# Test programs with no corresponding .in files are expected to fail
# at compile-time.  The error message is not controlled.  This script
# should be run from the directory containing the compiler sources,
# but can be run from anywhere, as long as you change RESDIR,
# COMPILER and MARS below.
#
# Written by Troels Henriksen  <athas@sigkill.dk>.

set -e # Die on first error.

# RESDIR is the path at which test programs can be found.
RESDIR=../DATA

# COMPILER is the command to run the compiler
COMPILER=../BIN/FastoC

# MARS is the command to run Mars.
# can be set e.g. $>MARS=/path/to/my/Mars_4_2.jar ./testg.sh
if [ -z "$MARS" ]; then
  MARS="$HOME/Mars_4_2.jar"
fi
RUNMARS="java -jar $MARS"

sh compile.sh

for FO in $RESDIR/*fo; do
    FO=$(basename $FO)
    PROG=$(echo $FO|sed 's/.fo$//')
    INPUT=$(echo $FO|sed 's/fo$/in/')
    OUTPUT=$(echo $FO|sed 's/fo$/out/')
    ERROUT=$(echo $FO|sed 's/fo$/err/')
    ASM=$(echo $FO|sed 's/fo$/asm/')
    TESTOUT=$RESDIR/$OUTPUT-testresult
    if [ -f $RESDIR/$INPUT ]; then
        echo Testing $FO:
        $COMPILER $RESDIR/$PROG
        if [ -f $RESDIR/$ASM ]; then
            touch $RESDIR/$INPUT
            $RUNMARS $RESDIR/$ASM < $RESDIR/$INPUT > $TESTOUT
            if [ -f $RESDIR/$OUTPUT ]; then
                if [ "$(cat $RESDIR/$OUTPUT|tail -n +2|tr -d [:space:])" !=\
                     "$(cat $TESTOUT|tail -n +2|tr -d [:space:])" ]; then
                    echo Output for $PROG does not match expected output.
                    echo Compare $TESTOUT and $RESDIR/$OUTPUT.
                else
                    rm -f $TESTOUT
                fi
            else
                rm -f $TESTOUT
            fi
        fi
    else
        echo "Testing $FO (expecting compiler error):"
        $COMPILER $RESDIR/$PROG > $TESTOUT 2>&1
        if cmp -s $TESTOUT /dev/null; then
            echo $PROG compiled, but should result in compile error.
            rm -f $TESTOUT
        elif [ -f $RESDIR/$ERROUT ]; then
            if ! cmp -s $RESDIR/$ERROUT $TESTOUT; then
                    echo Error message for $PROG does not match expected.
                    echo Compare $TESTOUT and $RESDIR/$ERROUT.
            else
                rm -f $TESTOUT
            fi
        else
            rm -f $TESTOUT
        fi
    fi
done
