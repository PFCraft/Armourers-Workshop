package goblinbob.mobends.core.client.gui.popup;

import goblinbob.mobends.core.client.gui.elements.GuiCompactTextField;
import net.minecraft.client.resources.I18n;

public class GuiPopUpCreatePack extends GuiPopUp
{

    protected GuiCompactTextField titleTextField;

    public GuiPopUpCreatePack()
    {
        super(I18n.format("mobends.gui.createpack"), new ButtonProps[] {
            new ButtonProps("Cancel", () -> {}),
            new ButtonProps("Create", () -> {})
        });
    }

    public void initGui(int x, int y)
    {
        super.initGui(x, y);
        titleTextField.setPosition(this.x + 5, this.y + 39);
    }

    public void display(int mouseX, int mouseY, float partialTicks)
    {
        super.display(mouseX, mouseY, partialTicks);
        titleTextField.drawTextBox();
    }

    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX, mouseY);
        if (titleTextField.isFocused())
            titleTextField.updateCursorCounter();
    }

    public void mouseClicked(int mouseX, int mouseY, int button)
    {
        titleTextField.mouseClicked(mouseX, mouseY, button);
        super.mouseClicked(mouseX, mouseY, button);
    }

    public void keyTyped(char typedChar, int keyCode)
    {
        titleTextField.textboxKeyTyped(typedChar, keyCode);
    }

}
