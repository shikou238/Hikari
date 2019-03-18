package shikou238.lwjgl.render.buffer.model

import shikou238.lwjgl.core.{Active, InActive, Renderable}

sealed trait Model{}
abstract class AnimateModel extends Model with Active
abstract class InanimateModel extends Model with InActive
