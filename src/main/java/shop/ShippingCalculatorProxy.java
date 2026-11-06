package shop;

public class ShippingCalculatorProxy implements ShippingCalculator {
    private final ShippingCalculator legacy;
    private final ShippingCalculator smart;
    private final FeatureFlags flags;

    public ShippingCalculatorProxy(ShippingCalculator legacy,
                                   ShippingCalculator smart,
                                   FeatureFlags flags) {
        this.legacy = legacy;
        this.smart = smart;
        this.flags = flags;
    }

    @Override
    public double calculate(Order order) {
        return flags.isEnabled("smart-shipping") && enCanary(order)
                ? smart.calculate(order)
                : legacy.calculate(order);
    }

    // Canary: solo ~20% de los clientes usan el cálculo nuevo
    private boolean enCanary(Order order) {
        return Math.abs(order.customerId().hashCode()) % 100 < 20;
    }
}