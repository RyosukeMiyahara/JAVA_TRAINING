/*
 * 練習問題17.3 p.406
 * ハッシュコードを使用する代わりに、キーを管理することで参照オブジェクトを使用するように、
 * リソース実装クラスを書き直しなさい。
 */

package ch17.ex17_03;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager
{
    final ReferenceQueue<Object> queue;
    final Map<Reference<?>, Resource> refs;
    final Thread reaper;
    boolean shutdown = false;

    public ResourceManager()
    {
        queue = new ReferenceQueue<Object>();
        refs = new HashMap<Reference<?>, Resource>();
        reaper = new ReaperThread();
        reaper.start();

        // ... リソースの初期化 ...
    }

    public synchronized void shutdown()
    {
        if (!shutdown)
        {
            shutdown = true;
            reaper.interrupt();
        }
    }

    public synchronized Resource getResource(Object key)
    {
        if (shutdown)
        {
            throw new IllegalStateException();
        }
        Resource res = new ResourceImpl(key);
        Reference<?> ref = new PhantomReference<Object>(key, queue);
        refs.put(ref, res);
        return res;
    }

    private static class ResourceImpl implements Resource
    {
        // int keyHash;
        SoftReference<Object> implKey;

        boolean needsRelease = false;

        ResourceImpl(Object key)
        {
            // keyHash = System.identityHashCode(key);
            implKey = new SoftReference<Object>(key);

            // .. 外部リソースの設定

            needsRelease = true;
        }

        public void use(Object key, Object... args)
        {
            // if (System.identityHashCode(key) != keyHash)
            if (!implKey.get().equals(key))
            {
                throw new IllegalArgumentException("wrong key");
            }
            // ... リソースの使用 ...
            System.out.println(key.toString() + " is used. ");
        }

        public synchronized void release()
        {
            if (needsRelease)
            {
                needsRelease = false;

                // ... リソースの解放 ...
                implKey.clear();
            }
        }
    }


    class ReaperThread extends Thread
    {
        public void run()
        {
            // 割り込まれるまで実行
            while(true)
            {
                try
                {
                    Reference<?> ref = queue.remove();
                    Resource res = null;
                    synchronized(ResourceManager.this)
                    {
                        res = refs.get(ref);
                        refs.remove(ref);
                    }
                    res.release();
                    ref.clear();
                    System.out.println("reaper!");
                }
                catch (InterruptedException ex)
                {
                    break; // すべて終了
                }
            }
        }
    }

    public static void main(String[] args)
    {
        ResourceManager test = new ResourceManager();

        System.out.println("resource1");

        String key1 = "key1";

        Resource resource1 = test.getResource(key1);
        resource1.use(key1);
        resource1.release();

        System.out.println("resource2");

        String key2 = "key2";

        try
        {
            Resource resource2 = test.getResource(key2);
            resource2.use(key1);
            resource2.release();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        test.shutdown();
    }
}
