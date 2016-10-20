package rulibutien.lgfana.gen;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LgfanaSpawnGenerator {

    public final static int OffsetSpawnY = 200;

    private World world;

    public LgfanaSpawnGenerator(World world) {
        this.world = world;
    }

    public void generate() {

        ChunkCoordinates spawn = this.world.getSpawnPoint();
        int x = spawn.posX;
        int z = spawn.posZ;

        cleanIsland();
        Schematic.spawn(this.world, x, OffsetSpawnY, z);

    }

    public void vanish() {

        ChunkCoordinates spawn = this.world.getSpawnPoint();
        int x = spawn.posX;
        int z = spawn.posZ;

        Schematic.deSpawn(this.world, x, OffsetSpawnY, z);
        cleanIsland();

    }

    private void cleanIsland() {

        File file = new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath() + "/lgfana");

        if (file.exists()) {

            try {

                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList list = (ArrayList) ois.readObject();
                ois.close();
                fis.close();

                for (Object li : list) {
                    if (li instanceof ArrayList) {

                        ArrayList ls = (ArrayList) li;

                        if (ls.get(0) instanceof Integer && ls.get(1) instanceof Integer && ls.get(2) instanceof Integer) {

                            int x = (Integer) ls.get(0);
                            int y = (Integer) ls.get(1);
                            int z = (Integer) ls.get(2);
                            world.setBlockToAir(x, y, z);

                        }

                    }

                }

                file.delete();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

    }

}