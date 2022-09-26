#!/bin/bash
# zero out any previously generated tags file
# an alternative way to achieve this is by `> tags`  
> tags 
find . -name \*.java -exec ctags --append {} \;
find ~/code/allwpilib -name \*.java -exec ctags --append {} \;
