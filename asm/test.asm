  OUT 7
  ADD A,1
  JNC 1
  ADD A,1
  JNC 3
  OUT 6
  ADD A,1
  JNC 6
  ADD A,1
  JNC 8
  OUT 0
  OUT 4
  ADD A,1
  JNC 10
  OUT 8
  JMP 15                        ; infinite loop
