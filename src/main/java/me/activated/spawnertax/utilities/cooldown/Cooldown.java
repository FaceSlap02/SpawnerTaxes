package me.activated.spawnertax.utilities.cooldown;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.text.DecimalFormat;

@Getter
@Setter
public class Cooldown {

    private DecimalFormat SECONDS_FORMAT = new DecimalFormat("#0.0");

    private long start = System.currentTimeMillis();
    private long expire;

    private Location location;

    public Cooldown(int seconds, Location location) {
        this.expire = this.start + (1000 * seconds);
        this.location = location;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return System.currentTimeMillis() - this.expire >= 1;
    }

    public int getSecondsLeft() {
        return (int) this.getRemaining() / 1000;
    }

    public String getMiliSecondsLeft() {
        return this.formatMilis(this.getRemaining());
    }

    public void cancel() {
        this.expire = 0;
    }

    private String formatMilis(long time) {
        return SECONDS_FORMAT.format(time / 1000.0F);
    }
}
