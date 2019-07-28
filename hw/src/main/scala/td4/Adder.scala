package td4

import chisel3._

class Adder extends Module {
  val io = IO(new Bundle{
    val in_a = Input(UInt(4.W))
    val in_b = Input(UInt(4.W))

    val out = Output(UInt(4.W))
  })

  io.out := io.in_a + io.in_b
}
