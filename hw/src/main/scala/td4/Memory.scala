package td4

import chisel3._
import chisel3.util.experimental.loadMemoryFromFile

class InstMemory(memfile: String) extends Module {
  val io = IO(new Bundle{
    val read_addr = Input(UInt(4.W))

    val read_data = Output(UInt(8.W))
  })

  val memory = Mem(16, UInt(8.W))
  // NOTE:
  loadMemoryFromFile(memory, memfile)

  io.read_data := memory(io.read_addr)
}
