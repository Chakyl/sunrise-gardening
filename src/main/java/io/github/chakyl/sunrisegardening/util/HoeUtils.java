package io.github.chakyl.sunrisegardening.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HoeUtils {
    public static AABB getHoeArea(int level, Player player, BlockPos pos) {
        int forward = 0;
        int back = 0;
        int left = 0;
        int right = 0;

        switch (level) {
            case 1:
                forward = 2;
                break;
            case 2:
                forward = 2;
                left = 1;
                right = 1;
                break;
            case 3:
                forward = 4;
                left = 1;
                right = 1;
                break;
            case 4:
                forward = 4;
                left = 2;
                right = 2;
                break;
            default:
                break;
        }

        Direction facing = player.getDirection();

        BlockPos start = pos;
        BlockPos end = pos;


        switch (facing) {
            case NORTH:
                start = pos.offset(-left, 0, -forward);
                end = pos.offset(right, 0, back);
                break;
            case SOUTH:
                start = pos.offset(-right, 0, -back);
                end = pos.offset(left, 0, forward);
                break;
            case WEST:
                start = pos.offset(-forward, 0, right);
                end = pos.offset(back, 0, -left);
                break;
            case EAST:
                start = pos.offset(forward, 0, -right);
                end = pos.offset(-back, 0, left);
                break;
            default:
                break;
        }

        return new AABB(Vec3.atLowerCornerOf(start), Vec3.atLowerCornerOf(end));
    }
}
