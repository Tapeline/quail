package me.tapeline.quailj.parsing.nodes.branching;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.block.BlockNode;

import java.util.ArrayList;
import java.util.List;

public class IfNode extends Node {

    public List<BlockNode> branches;
    public BlockNode elseBranch;
    public List<Node> conditions;

    public IfNode(Token token, List<Node> conditions, List<BlockNode> branches, BlockNode elseBranch) {
        super(token);
        this.branches = branches;
        this.elseBranch = elseBranch;
        this.conditions = conditions;
    }

    public IfNode(Token token, Node condition, BlockNode branch) {
        super(token);
        this.branches = new ArrayList<>();
        this.branches.add(branch);
        this.elseBranch = new BlockNode(token, new ArrayList<>());
        this.conditions = new ArrayList<>();
        this.conditions.add(condition);
    }

    public IfNode(Token token, BlockNode branch, Node condition, BlockNode elseBranch) {
        super(token);
        this.branches = new ArrayList<>();
        this.branches.add(branch);
        this.elseBranch = elseBranch;
        this.conditions = new ArrayList<>();
        this.conditions.add(condition);
    }

}
