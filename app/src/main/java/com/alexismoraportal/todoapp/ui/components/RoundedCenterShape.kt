package com.alexismoraportal.todoapp.ui.components

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * roundedCenterShape
 *
 * This Shape draws:
 *
 *  1) Rounded top-left and top-right corners, using [topCornerRadius].
 *  2) A central curved “semicircle” notch (using a cubic bezier), with an overall width
 *     of [circleDiameter] and controlled depth via [depthFactor].
 *  3) Small “internal arcs” of radius [internalCornerRadius] at both ends of the notch,
 *     to avoid sharp edges where the notch begins/ends.
 *  4) A clamp so that the notch is centered and does not collide with, or exceed, the
 *     corners on each side. If [circleDiameter] plus internal arcs are too large,
 *     the radius is reduced to fit within the available space.
 *  5) A straight bottom edge.
 *
 * @param circleDiameter       The total width of the central notch (approximately its height is half of this).
 * @param topCornerRadius      The radius for the external top corners (left and right).
 * @param depthFactor          The vertical depth factor for the “semicircle” shape (e.g., 0.66f or 1f).
 * @param internalCornerRadius The radius for the small arcs that smooth the transitions
 *                             where the central curve begins/ends.
 */
@Composable
fun roundedCenterShape(
    circleDiameter: Dp,
    topCornerRadius: Dp,
    depthFactor: Float = 0.66f,
    internalCornerRadius: Dp = 8.dp
): Shape {
    val density = LocalDensity.current

    // Convert from Dp to pixels.
    val circleDiameterPx = with(density) { circleDiameter.toPx() }
    val desiredRadius = circleDiameterPx / 2f

    val extR = with(density) { topCornerRadius.toPx() }
    val inR = with(density) { internalCornerRadius.toPx() }

    return GenericShape { size, _ ->
        val w = size.width
        val h = size.height
        val centerX = w / 2f

        // Start drawing at the top-left corner, offset vertically by the corner radius.
        moveTo(0f, extR)

        // (1) Draw the top-left corner arc.
        arcTo(
            rect = Rect(0f, 0f, extR, extR),
            startAngleDegrees = 180f,  // from left to right
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Now we are at (extR, 0f).

        // (2) Calculate the maximum usable radius, given the remaining horizontal space
        //     after subtracting corners (extR) and the internal arcs (inR).
        val totalHorizontalSpace = w - 2f * (extR + inR)
        val maxRadius = totalHorizontalSpace / 2f
        val finalRadius = min(desiredRadius, maxRadius)

        // (3) Coordinates for the central “semicircle” (or cubic curve).
        //     We shift left/right by the finalRadius plus the internal arc radius (inR).
        val leftArcStartX = centerX - finalRadius - inR
        val leftArcEndX = centerX - finalRadius
        val rightArcStartX = centerX + finalRadius
        val rightArcEndX = centerX + finalRadius + inR

        // Draw a straight line to the small left internal arc start.
        lineTo(x = leftArcStartX, y = 0f)

        // Left internal arc (small arc).
        arcTo(
            rect = Rect(
                leftArcStartX,
                0f,
                leftArcEndX,
                inR
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Now we are at (leftArcEndX, inR).

        // Cubic bezier curve for the central notch.
        val p0x = leftArcEndX
        val p0y = inR
        val p3x = rightArcStartX
        val p3y = inR

        // Vertical “push” depth for the curve.
        val pushY = finalRadius * depthFactor

        cubicTo(
            /* p1.x */ p0x,         /* p1.y */ p0y + pushY,
            /* p2.x */ p3x,         /* p2.y */ p3y + pushY,
            /* p3.x */ p3x,         /* p3.y */ p3y
        )
        // Now we are at (rightArcStartX, inR).

        // Right internal arc (small arc).
        arcTo(
            rect = Rect(
                rightArcStartX,
                0f,
                rightArcEndX,
                inR
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Now we are at (rightArcEndX, 0f).

        // (4) Draw the top-right corner.
        lineTo(x = w - extR, y = 0f)
        arcTo(
            rect = Rect(
                w - extR,
                0f,
                w,
                extR
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        // Now we are at (w, extR).

        // (5) Draw the straight bottom edge.
        lineTo(x = w, y = h)
        lineTo(x = 0f, y = h)
        close()
    }
}
