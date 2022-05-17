package applications

import applications.MathFunctions.sign
import applications.MathFunctions.add
import applications.MathFunctions.addScaled
import applications.MathFunctions.cross
import applications.MathFunctions.dot
import applications.MathFunctions.headingVector
import applications.MathFunctions.isNormalizedEps
import applications.MathFunctions.magnitude
import applications.MathFunctions.scale
import applications.MathFunctions.sub
import utils.round
import utils.toRadians
import kotlin.math.*

data class Vec3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {
    override fun toString(): String = "Vec3(x: ${x round 3}, y: ${y round 3})"
}

object MathFunctions {
    fun dot(a: Vec3, b: Vec3): Double = (a.x * b.x + a.y * b.y + a.z * b.z)

    fun dot(a: Vec3): Double = dot(a, a)

    fun cross(a: Vec3, b: Vec3): Vec3 = Vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x)

    fun add(a: Vec3, b: Vec3): Vec3 = Vec3(a.x + b.x, a.y + b.y, a.z + b.z)

    fun sub(a: Vec3, b: Vec3): Vec3 = Vec3(a.x - b.x, a.y - b.y, a.z - b.z)

    fun scale(a: Vec3, s: Double): Vec3 = Vec3(a.x * s, a.y * s, a.z * s)

    fun addScaled(a: Vec3, b: Vec3, s: Double): Vec3 = Vec3(a.x + b.x * s, a.y + b.y * s, a.z + b.z * s)

    fun magnitude(a: Vec3): Double = sqrt(dot(a))

    fun Double.thetaVector(): Vec3 = Vec3(cos(this), sin(this))

    fun Double.headingVector(): Vec3 = Vec3(cos(this.toRadians()), sin(this.toRadians()))

    fun isNormalizedEps(a: Vec3, epsilon: Double): Boolean {
        val sqrMag = dot(a)
        return sqrMag >= (1.0 - epsilon) * (1.0 - epsilon) && sqrMag <= (1.0f + epsilon) * (1.0 + epsilon)
    }

    fun sign(a: Double): Double = if (a < 0.0) -1.0 else 1.0
}

data class BiarcInterpolationData(
    var biarc_center: Vec3 = Vec3(), var biarc_axis1: Vec3 = Vec3(), var biarc_axis2: Vec3 = Vec3(),
    var biarc_radius: Double = 0.0, var biarc_angle: Double = 0.0, var biarc_arcLen: Double = 0.0
)

data class SingleBiarcOutput(var arcCenter: Vec3, var arcRadius: Double, var arcAngle: Double)
fun SingleBiarc(point: Vec3, tangent: Vec3, pointToMid: Vec3): SingleBiarcOutput {
    require(isNormalizedEps(tangent, 0.01))
    val epsilon = 0.0001

    val mArcCenter: Vec3
    val mArcRadius: Double
    val mArcAngle: Double

    val normal = cross(pointToMid, tangent)
    val perpAxis = cross(tangent, normal)

    val denominator = 2.0 * dot(perpAxis, pointToMid)
    when {
        denominator.absoluteValue < epsilon -> {
            mArcCenter = addScaled(point, tangent, 0.5)
            mArcRadius = 0.0
            mArcAngle = 0.0
        }
        else -> {
            val centerDist = dot(pointToMid) / denominator
            mArcCenter = addScaled(point, perpAxis, centerDist)

            val perpAxisMag = magnitude(perpAxis)
            val radius = ((centerDist * perpAxisMag).absoluteValue)
            val angle: Double = if (radius < epsilon) {
                0.0
            } else {
                val invRadius = 1.0 / radius

                var centerToMidDir = sub(point, mArcCenter)
                val centerToEndDir = scale(centerToMidDir, invRadius)

                centerToMidDir = add(centerToMidDir, pointToMid)
                centerToMidDir = scale(centerToMidDir, invRadius)

                val twist = dot(perpAxis, pointToMid)

                (acos(dot(centerToEndDir, centerToMidDir)) * sign(twist))
            }
            mArcRadius = radius
            mArcAngle = angle
        }
    }
    return SingleBiarcOutput(mArcCenter, mArcRadius, mArcAngle)
}

data class ComputeBiarcsOutput(val pArc1: BiarcInterpolationData, val pArc2: BiarcInterpolationData)
fun ComputeBiarcs(p1: Vec3, t1: Vec3, p2: Vec3, t2: Vec3): ComputeBiarcsOutput {
    require(isNormalizedEps(t1, 0.01))
    require(isNormalizedEps(t2, 0.01))

    val mpArc1 = BiarcInterpolationData()
    val mpArc2 = BiarcInterpolationData()

    val pi = Math.PI
    val tau = 2.0 * pi
    val epsilon = 0.0001

    val v = sub(p2, p1)
    val vDotV = dot(v)

    if (vDotV < epsilon) {
        mpArc1.apply {
            biarc_center = p1
            biarc_radius = 0.0
            biarc_axis1 = v
            biarc_axis2 = v
            biarc_angle = 0.0
            biarc_arcLen = 0.0
        }
        mpArc2.apply {
            biarc_center = p1
            biarc_radius = 0.0
            biarc_axis1 = v
            biarc_axis2 = v
            biarc_angle = 0.0
            biarc_arcLen = 0.0
        }
        return ComputeBiarcsOutput(mpArc1, mpArc2)
    }

    val t = add(t1, t2)
    val vDotT = dot(v, t)
    val t1DotT2 = dot(t1, t2)
    val denominator = 2.0 * (1.0 - t1DotT2)

    val d: Double
    when {
        denominator < epsilon -> {
            val vDotT2 = dot(v, t2)
            if (vDotT2.absoluteValue < epsilon) {
                val vMag = sqrt(vDotV)
                val invVMagSqr = 1.0 / vDotV

                val planeNormal = cross(v, t2)
                val perpAxis = cross(planeNormal, v)

                val radius = vMag * 0.25

                val centerToP1 = scale(v, -0.25)
                mpArc1.apply {
                    biarc_center = sub(p1, centerToP1)
                    biarc_radius = radius
                    biarc_axis1 = centerToP1
                    biarc_axis2 = scale(perpAxis, radius * invVMagSqr)
                    biarc_angle = pi
                    biarc_arcLen = pi * radius
                }
                mpArc2.apply {
                    biarc_center = sub(p2, centerToP1)
                    biarc_radius = radius
                    biarc_axis1 = scale(centerToP1, -1.0)
                    biarc_axis2 = scale(perpAxis, -radius * invVMagSqr)
                    biarc_angle = pi
                    biarc_arcLen = pi * radius
                }
                return ComputeBiarcsOutput(mpArc1, mpArc2)
            } else {
                d = vDotV / (4.0 * vDotT2)
            }
        }
        else -> {
            val discriminant = vDotT * vDotT + denominator * vDotV
            d = (-vDotT + sqrt(discriminant)) / denominator
        }
    }

    var pm = sub(t1, t2)
    pm = addScaled(p2, pm, d)
    pm = add(pm, p1)
    pm = scale(pm, 0.5)

    val p1ToPm = sub(pm, p1)
    val p2ToPm = sub(pm, p2)

    val biarcInterp1 = SingleBiarc(p1, t1, p1ToPm)
    val biarcInterp2 = SingleBiarc(p2, t2, p2ToPm)

    val center1 = biarcInterp1.arcCenter
    val radius1 = biarcInterp1.arcRadius
    var angle1 = biarcInterp1.arcAngle

    val center2 = biarcInterp2.arcCenter
    val radius2 = biarcInterp2.arcRadius
    var angle2 = biarcInterp2.arcAngle

    if (d < 0.0) {
        angle1 = sign(angle1) * tau - angle1
        angle2 = sign(angle2) * tau - angle2
    }

    mpArc1.apply {
        biarc_center = center1
        biarc_radius = radius1
        biarc_axis1 = sub(p1, center1)
        biarc_axis2 = scale(t1, radius1)
        biarc_angle = angle1
        biarc_arcLen = if (radius1 == 0.0) magnitude(p1ToPm) else abs(radius1 * angle1)
    }

    mpArc2.apply {
        biarc_center = center2
        biarc_radius = radius2
        biarc_axis1 = sub(p2, center2)
        biarc_axis2 = scale(t2, -radius2)
        biarc_angle = angle2
        biarc_arcLen = if (radius2 == 0.0) magnitude(p2ToPm) else abs(radius2 * angle2)
    }
    return ComputeBiarcsOutput(mpArc1, mpArc2)
}

data class BiarcInterpolationOutput(val vector: Vec3, val theta: Double)
fun BiarcInterpolation(arc1: BiarcInterpolationData, arc2: BiarcInterpolationData, frac: Double): BiarcInterpolationOutput {
    require(frac in 0.0..1.0)
    var pResult: Vec3

    val epsilon = 0.0001
    val totalDist = arc1.biarc_arcLen + arc2.biarc_arcLen
    val fracDist = frac * totalDist

    var m_angle = 0.0

    when {
        fracDist < arc1.biarc_arcLen -> {
            if (arc1.biarc_arcLen < epsilon) {
                pResult = add(arc1.biarc_center, arc1.biarc_axis1)
            } else {
                val arcFrac = fracDist / arc1.biarc_arcLen
                if (arc1.biarc_radius == 0.0) {
                    pResult = addScaled(arc1.biarc_center, arc1.biarc_axis1, -arcFrac * 2.0 + 1.0)
                } else {
                    val angle = arc1.biarc_angle * arcFrac
                    m_angle = angle
                    val sinRot = sin(angle)
                    val cosRot = cos(angle)
                    pResult = addScaled(arc1.biarc_center, arc1.biarc_axis1, cosRot)
                    pResult = addScaled(pResult, arc1.biarc_axis2, sinRot)
                }
            }
        }
        else -> {
            if (arc2.biarc_arcLen < epsilon) {
                pResult = add(arc2.biarc_center, arc2.biarc_axis1)
            } else {
                val arcFrac = (fracDist-arc1.biarc_arcLen) / arc2.biarc_arcLen
                if (arc2.biarc_radius == 0.0) {
                    pResult = addScaled(arc2.biarc_center, arc2.biarc_axis1, arcFrac * 2.0 - 1.0)
                } else {
                    val angle = arc2.biarc_angle * (1.0 - arcFrac)
                    m_angle = angle
                    val sinRot = sin(angle)
                    val cosRot = cos(angle)
                    pResult = addScaled(arc2.biarc_center, arc2.biarc_axis1, cosRot)
                    pResult = addScaled(pResult, arc2.biarc_axis2, sinRot)
                }
            }
        }
    }
    return BiarcInterpolationOutput(pResult, m_angle)
}
fun BiarcInterpolation(arc: ComputeBiarcsOutput, frac: Double): BiarcInterpolationOutput = BiarcInterpolation(arc.pArc1, arc.pArc2, frac)

infix fun ComputeBiarcsOutput.interpolationAt(frac: Double): BiarcInterpolationOutput = BiarcInterpolation(this, frac)

fun main() {
    val p1 = Vec3(200.0, 150.0)
    val p2 = Vec3(400.0, 350.0)
    val t1 = (0.0).headingVector()
    val t2 = (0.0).headingVector()

    val arc = ComputeBiarcs(p1, t1, p2, t2)
    val p = arc interpolationAt 0.5

    println("c1 center: ${arc.pArc1.biarc_center}, c1 angle: ${arc.pArc1.biarc_angle round 5}, c1 radius: ${arc.pArc1.biarc_radius round 3}")
    println("c2 center: ${arc.pArc2.biarc_center}, c2 angle: ${arc.pArc2.biarc_angle round 5}, c2 radius: ${arc.pArc2.biarc_radius round 3}")
    println("p vec: ${p.vector}, p angle: ${p.theta round 5}")
}