package td4

import chisel3._
import treadle._

class SimulatorOptionsManager extends TreadleOptionsManager

object SingleStep {
  def main(args: Array[String]): Unit = {
    val (_, firrtlSource) = build(args)
    val optionsManager = new SimulatorOptionsManager

    optionsManager.setTargetDirName("simulator_run_dir")

    val simulator = TreadleTester(firrtlSource, optionsManager)

    simulator.pokeMemory("mem.memory", 0, 1)
    simulator.pokeMemory("mem.memory", 1, 1)

    val pc_0 = simulator.peek("cpu.pc")
    val reg_a0 = simulator.peek("cpu.registers.regs_0")

    simulator.step()

    val pc_1 = simulator.peek("cpu.pc")
    val reg_a1 = simulator.peek("cpu.registers.regs_0")

    simulator.step()

    val pc_2 = simulator.peek("cpu.pc")
    val reg_a2 = simulator.peek("cpu.registers.regs_0")

    println(s"program counter is $pc_0 $pc_1 $pc_2")
    println(s"register a is $reg_a0 $reg_a1 $reg_a2")
  }

  def build(args: Array[String]): (String, String) = {
    chisel3.Driver.execute(args, () => new Top) match {
      case ChiselExecutionSuccess(Some(_), firrtlSource, Some(firrtlExecutionResult)) => {
        firrtlExecutionResult match {
          case firrtl.FirrtlExecutionSuccess(_, verilogSource) => {
            (verilogSource, firrtlSource)
          }
          case firrtl.FirrtlExecutionFailure(message) =>
            throw new Exception(s"FirrtlBackend: Compile failed.")
          case _ =>
            throw new Exception("Problem with compilation")
        }
      }
    }
  }
}
