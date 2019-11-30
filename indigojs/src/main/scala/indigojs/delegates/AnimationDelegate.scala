package indigojs.delegates

import indigojs.IndigoJSException
import scala.scalajs.js.annotation._
import scala.scalajs.js
import indigo.shared.animation.AnimationAction.JumpToFrame
import indigo.shared.animation.AnimationAction
import indigo.shared.animation.AnimationAction.Play
import indigo.shared.animation.AnimationAction.ChangeCycle
import indigo.shared.animation.AnimationAction.JumpToFirstFrame
import indigo.shared.animation.AnimationAction.JumpToLastFrame
import indigo.shared.animation.CycleLabel
import indigo.shared.animation.Animation
import indigo.shared.animation.AnimationKey
import indigo.shared.datatypes.ImageAssetRef
import indigo.shared.collections.NonEmptyList
import indigo.shared.animation.Frame
import indigo.shared.animation.Cycle
import indigo.shared.datatypes.Point

@SuppressWarnings(Array("org.wartremover.warts.Throw"))
@JSExportTopLevel("Animation")
final class AnimationDelegate(
    val animationsKey: String,
    val imageAssetRef: String,
    val spriteSheetWidth: Int,
    val spriteSheetHeight: Int,
    val cycles: js.Array[CycleDelegate]
) {
  def toInternal: Animation =
    NonEmptyList.fromList(cycles.map(_.toInternal).toList) match {
      case None =>
        throw new IndigoJSException("Animations cycle list cannot be empty.")

      case Some(animationsNel) =>
        new Animation(
          AnimationKey(animationsKey),
          ImageAssetRef(imageAssetRef),
          Point(spriteSheetWidth, spriteSheetHeight),
          animationsNel.head.label,
          animationsNel,
          Nil
        )
    }
}

@SuppressWarnings(Array("org.wartremover.warts.Throw"))
@JSExportTopLevel("Cycle")
final class CycleDelegate(val label: String, val frames: js.Array[FrameDelegate]) {
  def toInternal: Cycle =
    NonEmptyList.fromList(frames.map(_.toInternal).toList) match {
      case None =>
        throw new IndigoJSException("Cycle frames list cannot be empty.")

      case Some(framesNel) =>
        new Cycle(
          CycleLabel(label),
          framesNel,
          0,
          0
        )
    }
}

@JSExportTopLevel("Frame")
final class FrameDelegate(val bounds: RectangleDelegate, val duration: Int) {
  def toInternal: Frame =
    new Frame(bounds.toInternal, duration)
}

sealed trait AnimationActionDelegate {
  def toInternal: AnimationAction
}

@JSExportTopLevel("Play")
final class PlayDelegate extends AnimationActionDelegate {
  def toInternal: AnimationAction =
    Play
}

@JSExportTopLevel("ChangeCycle")
final class ChangeCycleDelegate(val label: String) extends AnimationActionDelegate {
  def toInternal: AnimationAction =
    ChangeCycle(CycleLabel(label))
}

@JSExportTopLevel("JumpToFirstFrame")
final class JumpToFirstFrameDelegate extends AnimationActionDelegate {
  def toInternal: AnimationAction =
    JumpToFirstFrame
}

@JSExportTopLevel("JumpToLastFrame")
final class JumpToLastFrameDelegate extends AnimationActionDelegate {
  def toInternal: AnimationAction =
    JumpToLastFrame
}

@JSExportTopLevel("JumpToFrame")
final class JumpToFrameDelegate(val number: Int) extends AnimationActionDelegate {
  def toInternal: AnimationAction =
    JumpToFrame(number)
}
