package team492;

import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import trclib.TrcUtil;

import java.util.Random;

import static org.junit.Assert.*;

public class TrajectoryCalculatorTest
{

    private void testSingleVertexConvergence(double y, double z)
    {
        RealVector vertexFromPivot = TrcUtil.createVector(y, z);
        RealVector traj = TrajectoryCalculator.calculateWithArmWithDrag(vertexFromPivot);
        assertNotNull(String.format("Failed for vertex: %s", vertexFromPivot.toString()), traj);
    }

    @Test
    public void testAngleConvergenceWithArm()
    {
        Random r = new Random();
        for (int a = 0; a < 50; a++)
        {
            testSingleVertexConvergence(r.nextInt(200) + 30, r.nextInt(60) + 30);
        }
        testSingleVertexConvergence(30, 20);
        testSingleVertexConvergence(48, 31);
    }
}