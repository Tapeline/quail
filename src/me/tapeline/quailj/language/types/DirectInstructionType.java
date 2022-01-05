package me.tapeline.quailj.language.types;

import java.util.HashMap;

public class DirectInstructionType extends QType {

    public final DirectInstruction i;
    public HashMap<String, QType> data = new HashMap<>();

    public DirectInstructionType(DirectInstruction di) {
        this.i = di;
    }

    @Override
    public String toString() {
        return i.equals(DirectInstruction.EXCEPTION)? "Exception:" + data.toString() :
                "DirectInstructionType" + data.toString();
    }

}
