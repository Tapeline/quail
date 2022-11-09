package me.tapeline.quailj.runtime.libraries.domain;

public class ComplexDomainNode {

    public DomainNode parentDomain;
    public SimpleDomainNode childDomain;

    public ComplexDomainNode(DomainNode parentDomain, SimpleDomainNode childDomain) {
        this.parentDomain = parentDomain;
        this.childDomain = childDomain;
    }

}
