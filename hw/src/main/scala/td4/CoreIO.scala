package td4

import chisel3._

class CoreIO extends Bundle {
  val in = Input(UInt(4.W))
  val read_data = Input(UInt(4.W))

  val out = Output(UInt(4.W))
  val read_addr = Output(UInt(4.W))
}
