package com.example.perf

import indigo._

import scala.util.Random
import scala.annotation.tailrec

object PerfView {

  val cloneId: CloneId = CloneId("Dude")

  def updateView(model: MyGameModel, inputSignals: InputSignals): SceneUpdateFragment = {
    inputSignals.mouseClickAt match {
      case Some(position) => println("Mouse clicked at: " + implicitly[AsString[Point]].show(position))
      case None           => ()
    }

    SceneUpdateFragment(
      gameLayer(model),
      lightingLayer(inputSignals),
      uiLayer(),
      Tint.White.withAmount(0.5),
      Nil,
      SceneAudio.None,
      ScreenEffects.None,
      Nil
    ).addCloneBlanks(
      CloneBlank(cloneId, model.dude.dude.sprite)
    )
  }

  private val herdCount: Int = 9999

  private val positions: List[Point] =
    (1 to herdCount).toList.map { _ =>
      Point((Random.nextFloat() * PerfGame.viewportWidth).toInt, (Random.nextFloat() * PerfGame.viewportHeight).toInt)
    }

  private val theHerd: List[CloneBatch] = {
    @tailrec
    def rec(remaining: List[Point], batchSize: Int, batchNumber: Int, acc: List[CloneBatch]): List[CloneBatch] =
      remaining match {
        case Nil =>
          acc

        case rs =>
          val (l, r) = rs.splitAt(batchSize)
          rec(
            r,
            batchSize,
            batchNumber + 1,
            CloneBatch(
              cloneId,
              Depth(1),
              CloneTransformData.identity,
              l.map(CloneTransformData.startAt),
              Some(BindingKey("herd" + batchNumber.toString))
            ) :: acc
          )
      }

    rec(positions, 256, 0, Nil)
  }

  def gameLayer(currentState: MyGameModel): List[SceneGraphNode] =
    List(
      currentState.dude.walkDirection match {
        case d @ DudeLeft =>
          currentState.dude.dude.sprite
            .changeCycle(d.cycleName)
            .play()

        case d @ DudeRight =>
          currentState.dude.dude.sprite
            .changeCycle(d.cycleName)
            .play()

        case d @ DudeUp =>
          currentState.dude.dude.sprite
            .changeCycle(d.cycleName)
            .play()

        case d @ DudeDown =>
          currentState.dude.dude.sprite
            .changeCycle(d.cycleName)
            .play()

        case d @ DudeIdle =>
          currentState.dude.dude.sprite
            .changeCycle(d.cycleName)
            .play()
      }
    ) ++ theHerd

  def lightingLayer(signals: InputSignals): List[SceneGraphNode] =
    List(
      Graphic(0, 0, 320, 240, 1, PerfAssets.light).withTint(1, 0, 0),
      Graphic(-115, -100, 320, 240, 1, PerfAssets.light),
      Graphic(signals.mousePosition.x - 160, signals.mousePosition.y - 120, 320, 240, 1, PerfAssets.light)
    )

  def fontKey: FontKey = FontKey("My Font")

  def fontInfo: FontInfo =
    FontInfo(fontKey, PerfAssets.smallFontName, 320, 230, FontChar("?", 93, 52, 23, 23))
      .addChar(FontChar("A", 3, 78, 23, 23))
      .addChar(FontChar("B", 26, 78, 23, 23))
      .addChar(FontChar("C", 50, 78, 23, 23))
      .addChar(FontChar("D", 73, 78, 23, 23))
      .addChar(FontChar("E", 96, 78, 23, 23))
      .addChar(FontChar("F", 119, 78, 23, 23))
      .addChar(FontChar("G", 142, 78, 23, 23))
      .addChar(FontChar("H", 165, 78, 23, 23))
      .addChar(FontChar("I", 188, 78, 15, 23))
      .addChar(FontChar("J", 202, 78, 23, 23))
      .addChar(FontChar("K", 225, 78, 23, 23))
      .addChar(FontChar("L", 248, 78, 23, 23))
      .addChar(FontChar("M", 271, 78, 23, 23))
      .addChar(FontChar("N", 3, 104, 23, 23))
      .addChar(FontChar("O", 29, 104, 23, 23))
      .addChar(FontChar("P", 54, 104, 23, 23))
      .addChar(FontChar("Q", 75, 104, 23, 23))
      .addChar(FontChar("R", 101, 104, 23, 23))
      .addChar(FontChar("S", 124, 104, 23, 23))
      .addChar(FontChar("T", 148, 104, 23, 23))
      .addChar(FontChar("U", 173, 104, 23, 23))
      .addChar(FontChar("V", 197, 104, 23, 23))
      .addChar(FontChar("W", 220, 104, 23, 23))
      .addChar(FontChar("X", 248, 104, 23, 23))
      .addChar(FontChar("Y", 271, 104, 23, 23))
      .addChar(FontChar("Z", 297, 104, 23, 23))
      .addChar(FontChar("0", 3, 26, 23, 23))
      .addChar(FontChar("1", 26, 26, 15, 23))
      .addChar(FontChar("2", 41, 26, 23, 23))
      .addChar(FontChar("3", 64, 26, 23, 23))
      .addChar(FontChar("4", 87, 26, 23, 23))
      .addChar(FontChar("5", 110, 26, 23, 23))
      .addChar(FontChar("6", 133, 26, 23, 23))
      .addChar(FontChar("7", 156, 26, 23, 23))
      .addChar(FontChar("8", 179, 26, 23, 23))
      .addChar(FontChar("9", 202, 26, 23, 23))
      .addChar(FontChar("?", 93, 52, 23, 23))
      .addChar(FontChar("!", 3, 0, 15, 23))
      .addChar(FontChar(".", 286, 0, 15, 23))
      .addChar(FontChar(",", 248, 0, 15, 23))
      .addChar(FontChar(" ", 145, 52, 23, 23))

  def uiLayer(): List[SceneGraphNode] =
    List(
      Text((herdCount + 1).toString + " Naked dudes!", PerfGame.viewportWidth / 2, 40, 5, fontKey).alignCenter,
      Text("Thundering Herd!", PerfGame.viewportWidth / 2, 10, 5, fontKey).alignCenter
    )

}
