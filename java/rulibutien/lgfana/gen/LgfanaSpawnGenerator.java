package rulibutien.lgfana.gen;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

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

        Schematic.spawn(world, x, OffsetSpawnY, z);

    }

    public void vanish() {

        ChunkCoordinates spawn = this.world.getSpawnPoint();
        int x = spawn.posX;
        int z = spawn.posZ;

        Schematic.deSpawn(world, x, OffsetSpawnY, z);

    }

}