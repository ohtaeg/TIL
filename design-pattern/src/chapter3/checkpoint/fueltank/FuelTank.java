package chapter3.checkpoint.fueltank;

import java.util.List;

public class FuelTank implements Monitorable {

    private List<Admin> admins;

    public FuelTank(final List<Admin> admins) {
        this.admins = admins;
    }

    @Override
    public void check() {
        if (checkFuelTank()) {
            warn();
        }
    }

    @Override
    public void warn() {
        for (Admin admin : admins) {
            admin.notify("경고");
        }
    }

    private boolean checkFuelTank() {
        return true;
    }
}
