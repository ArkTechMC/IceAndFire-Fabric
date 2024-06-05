package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityStymphalianBird;
import com.github.alexthe666.iceandfire.entity.ai.StymphalianBirdAIAirTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StymphalianBirdFlock {
    private EntityStymphalianBird leader;
    private ArrayList<EntityStymphalianBird> members = new ArrayList<>();
    private BlockPos leaderTarget;
    private BlockPos prevLeaderTarget;
    private Random random;
    private final int distance = 15;

    private StymphalianBirdFlock() {
    }

    public static StymphalianBirdFlock createFlock(EntityStymphalianBird bird) {
        StymphalianBirdFlock flock = new StymphalianBirdFlock();
        flock.leader = bird;
        flock.members = new ArrayList<>();
        flock.members.add(bird);
        flock.leaderTarget = bird.airTarget;
        flock.random = bird.getRandom();
        return flock;
    }

    public static StymphalianBirdFlock getNearbyFlock(EntityStymphalianBird bird) {
        float d0 = IafConfig.stymphalianBirdFlockLength;
        List<Entity> list = bird.getWorld().getOtherEntities(bird, (new Box(bird.getX(), bird.getY(), bird.getZ(), bird.getX() + 1.0D, bird.getY() + 1.0D, bird.getZ() + 1.0D)).expand(d0, 10.0D, d0), EntityStymphalianBird.STYMPHALIAN_PREDICATE);
        if (!list.isEmpty()) {
            Iterator<Entity> itr = list.iterator();
            while (itr.hasNext()) {
                Entity entity = itr.next();
                if (entity instanceof EntityStymphalianBird other) {
                    if (other.flock != null) {
                        return other.flock;
                    }
                }

            }
        }
        return null;
    }

    public boolean isLeader(EntityStymphalianBird bird) {
        return this.leader != null && this.leader == bird;
    }

    public void addToFlock(EntityStymphalianBird bird) {
        this.members.add(bird);
    }

    public void update() {
        if (!this.members.isEmpty() && (this.leader == null || !this.leader.isAlive())) {
            this.leader = this.members.get(this.random.nextInt(this.members.size()));
        }
        if (this.leader != null && this.leader.isAlive()) {
            this.prevLeaderTarget = this.leaderTarget;
            this.leaderTarget = this.leader.airTarget;
        }
    }

    public void onLeaderAttack(LivingEntity attackTarget) {
        for (EntityStymphalianBird bird : this.members) {
            if (bird.getTarget() == null && !this.isLeader(bird)) {
                bird.setTarget(attackTarget);
            }
        }
    }

    public EntityStymphalianBird getLeader() {
        return this.leader;
    }


    public void setTarget(BlockPos target) {
        this.leaderTarget = target;
        for (EntityStymphalianBird bird : this.members) {
            if (!this.isLeader(bird)) {
                bird.airTarget = StymphalianBirdAIAirTarget.getNearbyAirTarget(bird);
            }
        }
    }

    public void setFlying(boolean flying) {
        for (EntityStymphalianBird bird : this.members) {
            if (!this.isLeader(bird)) {
                bird.setFlying(flying);
            }
        }
    }

    public void setFearTarget(LivingEntity living) {
        for (EntityStymphalianBird bird : this.members) {
            bird.setVictor(living);
        }
    }
}
