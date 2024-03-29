package td4

import chisel3._

class Alu extends Module {
  val io = IO(new Bundle {
    val in_a = Input(UInt(4.W))
    val in_b = Input(UInt(4.W))

    val out_s = Output(UInt(4.W))
    val out_c = Output(Bool())
  })

  val sum = io.in_a +& io.in_b // carryありの演算 + であれば carryなし
  io.out_s := sum
  io.out_c := sum(4) // LSBからみて4bit目を取り出して、out_cに接続する
}
