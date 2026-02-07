package com.drool.practice.service;

import com.drool.practice.config.DroolConfig;
import com.drool.practice.model.Order;
import com.drool.practice.model.Rule;
import com.drool.practice.repository.DroolRulesRepo;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.drool.practice.config.DroolConfig.kieServices;

@Service
public class OrderService {
    private final KieContainer kieContainer;
    private DroolRulesRepo rulesRepo;

    public OrderService(KieContainer kieContainer, DroolRulesRepo rulesRepo) {
        this.kieContainer = kieContainer;
        this.rulesRepo = rulesRepo;
    }

    public Order getDiscountForOrder(Order order) {
        KieSession session = kieContainer.newKieSession();
        session.insert(order);
        session.fireAllRules();
        session.dispose();
        return order;
    }

    public Order getDiscountForOrderV2(Order order) {
        List<Rule> ruleAttributes = new ArrayList<>();
        rulesRepo.findAll().forEach(ruleAttributes::add);

        ObjectDataCompiler compiler = new ObjectDataCompiler();
        String generatedDRL = compiler.compile(ruleAttributes, Thread.currentThread().getContextClassLoader().getResourceAsStream(DroolConfig.RULES_TEMPLATE_FILE));
        KieServices kieService = KieServices.Factory.get();

        KieHelper kieHelper = new KieHelper();

        //multiple rules can be added to the kieHelper
        byte[] b1 = generatedDRL.getBytes();

        Resource resource1 = kieServices.getResources().newByteArrayResource(b1);
        kieHelper.addResource(resource1, ResourceType.DRL);

        KieBase kieBase = kieHelper.build();

        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(order);
        int numberOfRulesFired = kieSession.fireAllRules();
        kieSession.dispose();

        return order;
    }
}
