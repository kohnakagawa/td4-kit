package td4

import chisel3._
import chisel3.util._

class Decoder extends Module {
  val io = IO(new Bundle {
    val insn = Input(UInt(8.W))

    val valid_inst = Output(Bool())
    val read_reg = Output(UInt(2.W))
    val write_reg = Output(UInt(2.W))
    val write_en = Output(Bool())
    val in_en = Output(Bool())
    val out_en = Output(Bool())
    val alu_en = Output(Bool())
    val jmp_en = Output(Bool())
    val jnc_en = Output(Bool())
  })

  val opcode = io.insn(7, 4)

  val signals =
    ListLookup(opcode,
      List(false.B, 0.U, 0.U, false.B, false.B, false.B, false.B, false.B, false.B),
      Array(
        // MOV A,Im
        BitPat("b0011") -> List(true.B, 2.U, 0.U, true.B, false.B, false.B, false.B, false.B, false.B),
        // MOV B,Im
        BitPat("b0111") -> List(true.B, 2.U, 1.U, true.B, false.B, false.B, false.B, false.B, false.B),
        // MOV A,B
        BitPat("b0001") -> List(true.B, 1.U, 0.U, true.B, false.B, false.B, false.B, false.B, false.B),
        // MOV B,A
        BitPat("b0100") -> List(true.B, 1.U, 0.U, true.B, false.B, false.B, false.B, false.B, false.B),
        // ADD A,Im
        BitPat("b0000") -> List(true.B, 2.U, 0.U, true.B, false.B, false.B, true.B , false.B, false.B),
        // ADD B,Im
        BitPat("b0101") -> List(true.B, 2.U, 1.U, true.B, false.B, false.B, true.B , false.B, false.B),
        // IN A
        BitPat("b0010") -> List(true.B, 2.U, 0.U, true.B, true.B , false.B, false.B, false.B, false.B),
        // IN B
        BitPat("b0110") -> List(true.B, 2.U, 1.U, true.B, true.B , false.B, false.B, false.B, false.B),
        // OUT Im
        BitPat("b1011") -> List(true.B, 2.U, 0.U, false.B, false.B, true.B, true.B , false.B, false.B),
        // OUT B
        BitPat("b1001") -> List(true.B, 1.U, 0.U, false.B, false.B, true.B, true.B , false.B, false.B),
        // JMP Im
        BitPat("b1111") -> List(true.B, 0.U, 0.U, false.B, false.B, false.B, false.B, true.B, false.B),
        // JNC Im
        BitPat("b1110") -> List(true.B, 0.U, 0.U, false.B, false.B, false.B, false.B, false.B, true.B)
      )
    )

  io.valid_inst := signals(0)
  io.read_reg := signals(1)
  io.write_reg := signals(2)
  io.write_en := signals(3)
  io.in_en := signals(4)
  io.out_en := signals(5)
  io.alu_en := signals(6)
  io.jmp_en := signals(7)
  io.jnc_en := signals(8)
}
