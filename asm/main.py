import sys
import re
from enum import Enum, unique, auto
from argparse import ArgumentParser


@unique
class OperandType(Enum):
    A = auto()
    B = auto()
    IM = auto()
    A_IM = auto()
    B_IM = auto()
    A_B = auto()
    B_A = auto()


@unique
class OpType(Enum):
    MOV = auto()
    ADD = auto()
    IN = auto()
    OUT = auto()
    JMP = auto()
    JNC = auto()


RE_A_IM = re.compile(r"^A,([0-9]*)$")
RE_B_IM = re.compile(r"^B,([0-9]*)$")
RE_IM = re.compile(r"^([0-9]*)$")


def extract_im(pattern, operand_str):
    return int(pattern.match(operand_str).groups()[0])


def get_im(operand_type, operand_str):
    if operand_type == OperandType.A_IM:
        return extract_im(RE_A_IM, operand_str)
    elif operand_type == OperandType.B_IM:
        return extract_im(RE_B_IM, operand_str)
    elif operand_type == OperandType.IM:
        return extract_im(RE_IM, operand_str)
    else:
        return 0


def get_op_type(op_str):
    return OpType[op_str]


def get_operand_type(operand_str):
    if operand_str == "A":
        return OperandType.A
    elif operand_str == "B":
        return OperandType.B
    elif operand_str == "A,B":
        return OperandType.A_B
    elif operand_str == "B,A":
        return OperandType.B_A
    elif RE_A_IM.match(operand_str) is not None:
        return OperandType.A_IM
    elif RE_B_IM.match(operand_str) is not None:
        return OperandType.B_IM
    elif RE_IM.match(operand_str) is not None:
        return OperandType.IM
    else:
        raise ValueError("ill-formed perand".format(operand_str))


def normalize_inst_str(inst_str):
    return inst_str.split(";")[0].strip().upper()


def mix_op_and_im(op, im):
    return (op << 4) | im


def get_opcode(optype, operand_type, inst_str):
    err_msg = "{}: undefined instruction.".format(inst_str)
    if optype == OpType.MOV:
        if operand_type == OperandType.A_IM:
            return 0b0011
        elif operand_type == OperandType.B_IM:
            return 0b0111
        elif operand_type == OperandType.A_B:
            return 0b0001
        elif operand_type == OperandType.B_A:
            return 0b0100
        else:
            raise RuntimeError(err_msg)
    elif optype == OpType.ADD:
        if operand_type == OperandType.A_IM:
            return 0b0000
        elif operand_type == OperandType.B_IM:
            return 0b0101
        else:
            raise RuntimeError(err_msg)
    elif optype == OpType.IN:
        if operand_type == OperandType.A:
            return 0b0010
        elif operand_type == OperandType.B:
            return 0b0110
        else:
            raise RuntimeError(err_msg)
    elif optype == OpType.OUT:
        if operand_type == OperandType.IM:
            return 0b1011
        elif operand_type == OperandType.B:
            return 0b1001
        else:
            raise RuntimeError(err_msg)
    elif optype == OpType.JMP:
        if operand_type == OperandType.IM:
            return 0b1111
        else:
            raise RuntimeError(err_msg)
    elif optype == OpType.JNC:
        if operand_type == OperandType.IM:
            return 0b1110
        else:
            raise RuntimeError(err_msg)


class AsmInst:
    def __init__(self, inst_str):
        self.inst_str = inst_str

    def emit_code(self):
        opcode = get_opcode(self.op, self.operand, self.inst_str)
        return mix_op_and_im(opcode, self.im)

    def parse(self):
        self.inst_str = normalize_inst_str(self.inst_str)
        inst_str_splitted = self.inst_str.split(" ")
        if len(inst_str_splitted) != 2:
            print("{}: ill-formed instruction format".format(self.inst_str))
            return

        op_str, operand_str = inst_str_splitted
        self.op = get_op_type(op_str)
        self.operand = get_operand_type(operand_str)
        self.im = get_im(self.operand, operand_str)


def _main():
    parser = ArgumentParser(description="create a binary file from a TD4 assembly file.")
    parser.add_argument("-i", "--input", help="input assembly file")
    parser.add_argument("-o", "--output", help="output binary file")
    args = parser.parse_args()

    asm_file = args.input
    bin_file = args.output

    with open(asm_file, "r") as f:
        asm_lines = f.readlines()

    bins = b""
    for line in asm_lines:
        inst = AsmInst(line)
        inst.parse()
        bins += inst.emit_code().to_bytes(1, "little")

    with open(bin_file, "wb") as f:
        f.write(bins)


if __name__ == "__main__":
    _main()
