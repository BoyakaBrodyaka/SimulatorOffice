package org.example.stats.status;

public class Statuses {
    public class GoodStatus implements IncomeModifier {
        @Override
        public double modifyIncome(double baseIncome) {
            return baseIncome * 1.5;
        }
    }

    public class NeutralStatus implements IncomeModifier {
        @Override
        public double modifyIncome(double baseIncome) {
            return baseIncome;
        }
    }

    public class AverageStatus implements IncomeModifier {
        @Override
        public double modifyIncome(double baseIncome) {
            return baseIncome * 0.8;
        }
    }

    public class BadStatus implements IncomeModifier {
        @Override
        public double modifyIncome(double baseIncome) {
            return baseIncome * 0.5;
        }
    }

    public class TerribleStatus implements IncomeModifier {
        @Override
        public double modifyIncome(double baseIncome) {
            return 0;
        }
    }
}
