package rulibutien.lgfana.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;

import java.io.*;
import java.util.ArrayList;

import static rulibutien.lgfana.common.Lgfana.GAMERULE_GO;

public class PlacingBlock {

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void onPlacingBlock(PlaceEvent event) {

        World world = event.player.getEntityWorld();
        GameRules rules = world.getGameRules();

        if (!rules.getGameRuleBooleanValue(GAMERULE_GO)) {

            File file = new File(world.getSaveHandler().getWorldDirectory().getAbsolutePath() + "/lgfana");

            try {

                ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();

                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    list = (ArrayList<ArrayList<Integer>>) ois.readObject();

                    ois.close();
                    fis.close();
                }

                ArrayList<Integer> coords = new ArrayList<Integer>();
                coords.add(event.x);
                coords.add(event.y);
                coords.add(event.z);
                list.add(coords);

                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(list);
                oos.close();
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

    }

}
