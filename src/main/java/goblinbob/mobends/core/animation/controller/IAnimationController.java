package goblinbob.mobends.core.animation.controller;

import goblinbob.mobends.core.data.EntityData;
import java.util.Collection;
import javax.annotation.Nullable;

/*
 * This interface is responsible for updating animation for each
 * instance of an AnimatedEntity.
 * 
 * It's a member of an EntityData instance, and it holds all
 * the information about the current animation's state.
 * */
public interface IAnimationController<T extends EntityData<?>>
{

	@Nullable
	Collection<String> perform(T entityData);
	
}
