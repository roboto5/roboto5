import dev.robocode.tankroyale.botapi.*;
import dev.robocode.tankroyale.botapi.events.*;

// ------------------------------------------------------------------
// A3Bot
// ------------------------------------------------------------------
// A sample bot original made for Robocode by Mathew Nelson.
// Ported to Robocode Tank Royale by Flemming N. Larsen.
//
// Probably the first bot you will learn about.
// Moves in a seesaw motion, and spins the gun around at each end.
// ------------------------------------------------------------------
public class Botto extends Bot {

    int turnDirection = 1; // clockwise (-1) or counterclockwise (1)

    // The main method starts our bot
    public static void main(String[] args) {
        new Botto().start();
    }

    // Constructor, which loads the bot config file
    Botto() {
        super(BotInfo.fromFile("Botto.json"));
    }

    // Called when a new round is started -> initialize and do some movement
    @Override
    public void run() {
        setBodyColor(Color.PURPLE);
        setTurretColor(Color.TEAL);
        setRadarColor(Color.PURPLE);
        setBulletColor(Color.RED);
        setScanColor(Color.PURPLE);

        while (isRunning()) {
            turnLeft(5 * turnDirection);
        }
    }

    // We scanned another bot -> go ram it
    @Override
    public void onScannedBot(ScannedBotEvent e) {
        turnToFaceTarget(e.getX(), e.getY());

        var distance = distanceTo(e.getX(), e.getY());
        forward(distance + 5);

        rescan(); // Might want to move forward again!
    }

    // We have hit another bot -> turn to face bot, fire hard, and ram it again!
    @Override
    public void onHitBot(HitBotEvent e) {
        turnToFaceTarget(e.getX(), e.getY());

        // Determine a shot that won't kill the bot...
        // We want to ram him instead for bonus points
        if (e.getEnergy() > 16) {
            fire(3);
        } else if (e.getEnergy() > 10) {
            fire(2);
        } else if (e.getEnergy() > 4) {
            fire(1);
        } else if (e.getEnergy() > 2) {
            fire(.5);
        } else if (e.getEnergy() > .4) {
            fire(.1);
        }
        forward(40); // Ram him again!
    }

    // Method that turns the bot to face the target at coordinate x,y, but also sets the
    // default turn direction used if no bot is being scanned within in the run() method.
    private void turnToFaceTarget(double x, double y) {
        var bearing = bearingTo(x, y);
        if (bearing >= 0) {
            turnDirection = 1;
        } else {
            turnDirection = -1;
        }
        turnLeft(bearing);
    }

    @Override
    public void onBulletHitWall(BulletHitWallEvent e) {
        turnLeft(180);
        rescan();
    }
}