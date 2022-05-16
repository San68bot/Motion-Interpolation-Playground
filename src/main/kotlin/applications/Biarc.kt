package applications

import applications.mathFunctions.Sign
import applications.mathFunctions.add
import applications.mathFunctions.addScaled
import applications.mathFunctions.cross
import applications.mathFunctions.dot
import applications.mathFunctions.isNormalizedEsp
import applications.mathFunctions.magnitude
import applications.mathFunctions.scale
import applications.mathFunctions.sub
import kotlin.math.*

data class vec3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0)

object mathFunctions {
    fun dot(a: vec3, b: vec3): Float {
        return (a.x * b.x + a.y * b.y + a.z * b.z).toFloat()
    }

    fun cross(a: vec3, b: vec3): vec3 {
        return vec3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x)
    }

    fun add(a: vec3, b: vec3): vec3 {
        return vec3(a.x + b.x, a.y + b.y, a.z + b.z)
    }

    fun sub(a: vec3, b: vec3): vec3 {
        return vec3(a.x - b.x, a.y - b.y, a.z - b.z)
    }

    fun scale(a: vec3, s: Float): vec3 {
        return vec3(a.x * s, a.y * s, a.z * s)
    }

    fun addScaled(a: vec3, b: vec3, s: Float): vec3 {
        return vec3(a.x + b.x * s, a.y + b.y * s, a.z + b.z * s)
    }

    fun magnitude(a: vec3): Float {
        return sqrt(dot(a, a))
    }

    fun isNormalizedEsp(a: vec3, epsilon: Float): Boolean {
        val sqrMag = dot(a, a)
        return sqrMag >= (1.0f - epsilon) * (1.0f - epsilon) && sqrMag <= (1.0f + epsilon) * (1.0f + epsilon)
    }

    fun Sign(a: Float): Float {
        return if (a < 0.0f) -1.0f else 1.0f
    }
}

data class tBiarcInterp_Arc(
    var m_center: vec3 = vec3(), var m_axis1: vec3 = vec3(), var m_axis2: vec3 = vec3(),
    var m_radius: Float = 0f, var m_angle: Float = 0f, var m_arcLen: Float = 0f)

data class BiarcInterp_ComputeArcOUT(var pCenter: vec3, var pRadius: Float, var pAngle: Float)

fun BiarcInterp_ComputeArc(point: vec3, tangent: vec3, pointToMid: vec3): BiarcInterp_ComputeArcOUT {
    assert(isNormalizedEsp(tangent, 0.01f))
    val c_Epsilon = 0.0001f

    var mpCenter = vec3()
    var mpRadius = 0.0f
    var mpAngle = 0.0f

    val normal = cross(pointToMid, tangent)
    val perpAxis = cross(tangent, normal)

    val denominator = 2.0f * dot(perpAxis, pointToMid)
    if(denominator.absoluteValue < c_Epsilon) {
        mpCenter = addScaled(point, tangent, 0.5f)
        mpRadius = 0.0f
        mpAngle = 0.0f
    } else {
        val centerDist = dot(pointToMid, pointToMid) / denominator
        mpCenter = addScaled(point, perpAxis, centerDist.toFloat())

        // Compute the radius in absolute units
        val perpAxisMag = magnitude(perpAxis)
        val radius = ((centerDist*perpAxisMag).absoluteValue).toFloat()
        val angle: Float = if (radius < c_Epsilon) {
            0.0f
        } else {
            val invRadius = 1.0f / radius

            var centerToMidDir = sub(point, mpCenter)
            val centerToEndDir = scale(centerToMidDir, invRadius)

            centerToMidDir = add(centerToMidDir, pointToMid)
            centerToMidDir = scale(centerToMidDir, invRadius)

            val twist = dot(perpAxis, pointToMid)

            (acos(dot(centerToEndDir,centerToMidDir)) * Sign(twist))
        }
        mpRadius = radius
        mpAngle = angle
    }
    return BiarcInterp_ComputeArcOUT(mpCenter, mpRadius, mpAngle)
}

data class BiarcInterp_ComputeArcsOUT(val pArc1: tBiarcInterp_Arc, val pArc2: tBiarcInterp_Arc)


fun BiarcInterp_ComputeArcs(p1: vec3, t1: vec3, p2: vec3, t2: vec3): BiarcInterp_ComputeArcsOUT {
    assert(isNormalizedEsp(t1, 0.01f))
    assert(isNormalizedEsp(t2, 0.01f))

    val mpArc1 = tBiarcInterp_Arc()
    val mpArc2 = tBiarcInterp_Arc()

    val c_pi = Math.PI.toFloat()
    val c_2Pi = 2.0f * c_pi
    val c_Epsilon = 0.0001f

    val v = sub(p2, p1)
    val vDotV = dot(v, v)

    if (vDotV < c_Epsilon) {
        mpArc1.apply {
            m_center = p1
            m_radius = 0.0f
            m_axis1 = v
            m_axis2 = v
            m_angle = 0.0f
            m_arcLen = 0.0f
        }
        mpArc2.apply {
            m_center = p1
            m_radius = 0.0f
            m_axis1 = v
            m_axis2 = v
            m_angle = 0.0f
            m_arcLen = 0.0f
        }
        return BiarcInterp_ComputeArcsOUT(mpArc1, mpArc2)
    }

    val t = add(t1, t2)

    val vDotT = dot(v, t)
    val t1DotT2 = dot(t1, t2)
    val denominator = 2.0f * (1.0f - t1DotT2)

    var d = 0.0f
    if (denominator < c_Epsilon) {
        val vDotT2 = dot(v, t2)
        if (vDotT2.absoluteValue < c_Epsilon) {
            val vMag = sqrt(vDotV)
            val invVMagSqr = 1.0f / vDotV

            val planeNormal = cross(v, t2)
            val perpAxis = cross(planeNormal, v)

            val radius = vMag * 0.25f

            val centerToP1 = scale(v, -0.25f)
            mpArc1.apply {
                m_center = sub(p1, centerToP1)
                m_radius = radius
                m_axis1 = centerToP1
                m_axis2 = scale(perpAxis, radius * invVMagSqr)
                m_angle = c_pi
                m_arcLen = c_pi * radius
            }
            mpArc2.apply {
                m_center = sub(p2, centerToP1)
                m_radius = radius
                m_axis1 = scale(centerToP1, -1.0f)
                m_axis2 = scale(perpAxis, -radius * invVMagSqr)
                m_angle = c_pi
                m_arcLen = c_pi * radius
            }
            return BiarcInterp_ComputeArcsOUT(mpArc1, mpArc2)
        } else {
            d = vDotV / (4.0f * vDotT2)
        }
    } else {
        val discriminant = vDotT * vDotT + denominator * vDotV
        d = (-vDotT + sqrt(discriminant)) / denominator
    }

    var pm = sub(t1, t2)
    pm = addScaled(p2, pm, d)
    pm = add(pm, p1)
    pm = scale(pm, 0.5f)

    val p1ToPm = sub(pm, p1)
    val p2ToPm = sub(pm, p2)

    val BiarcInterp_ComputeArc_1 = BiarcInterp_ComputeArc(p1, t1, p1ToPm)
    val BiarcInterp_ComputeArc_2 = BiarcInterp_ComputeArc(p2, t2, p2ToPm)

    val center1 = BiarcInterp_ComputeArc_1.pCenter
    val radius1 = BiarcInterp_ComputeArc_1.pRadius
    var angle1 = BiarcInterp_ComputeArc_1.pAngle

    val center2 = BiarcInterp_ComputeArc_2.pCenter
    val radius2 = BiarcInterp_ComputeArc_2.pRadius
    var angle2 = BiarcInterp_ComputeArc_2.pAngle

    if (d < 0.0f) {
        angle1 = Sign(angle1) * c_2Pi - angle1
        angle2 = Sign(angle2) * c_2Pi - angle2
    }

    mpArc1.apply {
        m_center = center1
        m_radius = radius1
        m_axis1 = sub(p1, center1)
        m_axis2 = scale(t1, radius1)
        m_angle = angle1
        m_arcLen = if (radius1 == 0.0f) magnitude(p1ToPm) else abs(radius1 * angle1)
    }

    mpArc2.apply {
        m_center = center2
        m_radius = radius2
        m_axis1 = sub(p2, center2)
        m_axis2 = scale(t2, -radius2)
        m_angle = angle2
        m_arcLen = if (radius2 == 0.0f) magnitude(p2ToPm) else abs(radius2 * angle2)
    }

    return BiarcInterp_ComputeArcsOUT(mpArc1, mpArc2)
}

fun BiarcInterp(arc1: tBiarcInterp_Arc, arc2: tBiarcInterp_Arc, frac: Float): vec3 {
    assert(frac in 0.0f..1.0f);
    var pResult: vec3

    val epsilon = 0.0001f

    val totalDist = arc1.m_arcLen + arc2.m_arcLen
    val fracDist = frac * totalDist

    if (fracDist < arc1.m_arcLen) {
        if (arc1.m_arcLen < epsilon) {
            pResult = add(arc1.m_center, arc1.m_axis1)
        } else {
            val arcFrac = fracDist / arc1.m_arcLen
            if (arc1.m_radius == 0.0f) {
                pResult = addScaled(arc1.m_center, arc1.m_axis1, -arcFrac*2.0f + 1.0f)
            } else {
                val angle = arc1.m_angle * arcFrac
                val sinRot = sin(angle)
                val cosRot = cos(angle)
                pResult = addScaled(arc1.m_center, arc1.m_axis1, cosRot)
                pResult = addScaled(pResult, arc1.m_axis2, sinRot)
            }
        }
    } else {
        if (arc2.m_arcLen < epsilon) {
            pResult = add(arc2.m_center, arc2.m_axis1)
        } else {
            val arcFrac = (fracDist - arc1.m_arcLen) / arc2.m_arcLen
            if (arc2.m_radius == 0.0f) {
                pResult = addScaled(arc2.m_center, arc2.m_axis1, arcFrac * 2.0f - 1.0f)
            } else {
                val angle = arc2.m_angle * (1.0f - arcFrac)
                val sinRot = sin(angle)
                val cosRot = cos(angle)
                pResult = addScaled(arc2.m_center, arc2.m_axis1, cosRot)
                pResult = addScaled(pResult, arc2.m_axis2, sinRot)
            }
        }
    }
    return pResult
}