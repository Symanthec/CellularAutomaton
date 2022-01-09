package app.ui.panes;

import app.rendering.FrameBuffer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import java.util.zip.Deflater;

public class TextureExporter implements Disposable {

    private FrameBuffer fbo;

    public TextureExporter() {
        fbo = new FrameBuffer();
    }

    public void saveImage(Texture tex, FileHandle handle) {
        fbo.bind();
        fbo.useTexture(tex);
        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, tex.getWidth(), tex.getHeight());
        PixmapIO.writePNG(handle, pixmap, Deflater.DEFAULT_COMPRESSION, false);
        fbo.unbind();
    }

    @Override
    public void dispose() {
        fbo.dispose();
    }
}
