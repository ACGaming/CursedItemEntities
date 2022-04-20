package mod.acgaming.cie.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import mod.acgaming.cie.config.CIEConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderEntityItem.class)
public abstract class RenderEntityItemMixin extends Render<EntityItem>
{
    protected RenderEntityItemMixin(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Shadow
    protected int getModelCount(ItemStack stack)
    {
        int i = 1;

        if (stack.getCount() > 48)
        {
            i = 5;
        }
        else if (stack.getCount() > 32)
        {
            i = 4;
        }
        else if (stack.getCount() > 16)
        {
            i = 3;
        }
        else if (stack.getCount() > 1)
        {
            i = 2;
        }

        return i;
    }

    /**
     * @author ACGaming
     */
    @Overwrite
    private int transformModelCount(EntityItem itemIn, double x, double y, double z, float partialTicks, IBakedModel ibakedmodel)
    {
        ItemStack itemstack = itemIn.getItem();

        boolean flag = ibakedmodel.isGui3d();
        int i = this.getModelCount(itemstack);
        float f1 = CIEConfig.shouldBob ? MathHelper.sin(((float) itemIn.getAge() + partialTicks) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0;
        float f2 = CIEConfig.shouldBob ? ibakedmodel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y : 0;

        if (CIEConfig.shouldBob) GlStateManager.translate((float) x, (float) y + f1 + 0.25F * f2, (float) z);
        else GlStateManager.translate((float) x, (float) y - 0.05F, (float) z);

        if (flag || this.renderManager.options != null)
        {
            float f3 = CIEConfig.shouldRotate ? (((float) itemIn.getAge() + partialTicks) / 20.0F + itemIn.hoverStart) * (180F / (float) Math.PI) : 0;
            if (CIEConfig.shouldRotate) GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        return i;
    }
}