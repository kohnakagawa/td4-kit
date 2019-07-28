package td4

import chisel3._

class ImmGen extends Module {
  val io = IO(new Bundle {
    val insn = Input(UInt(8.W))

    val imm = Output(UInt(4.W))
  })

  io.imm := io.insn(3, 0)
}
