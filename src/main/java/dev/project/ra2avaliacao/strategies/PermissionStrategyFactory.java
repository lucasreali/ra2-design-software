package dev.project.ra2avaliacao.strategies;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PermissionStrategyFactory {

    private final Map<String, PermissionStrategy> strategies;

    public PermissionStrategyFactory(List<PermissionStrategy> permissionStrategies) {
        this.strategies = new HashMap<>();
        for (PermissionStrategy strategy : permissionStrategies) {
            strategies.put(strategy.getStrategyName(), strategy);
        }
    }

    public PermissionStrategy getStrategy(String strategyName) {
        PermissionStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown permission strategy: " + strategyName);
        }
        return strategy;
    }
}
