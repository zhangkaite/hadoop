package zkt.hdfs.test;

import java.io.IOException;  
import java.io.RandomAccessFile;  
import java.nio.ByteBuffer;  
import java.nio.MappedByteBuffer;  
import java.nio.channels.FileChannel;  
import java.nio.channels.FileLock;  
  
public class LockingMappedFiles {  
    static final int LENGTH = 0x200000; // 2 Mb  
    //static final int LENGTH = 100;  
    static FileChannel fc;  
  
    @SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {  
        //使用可随机访问文件创建可读写文件通道  
        fc = new RandomAccessFile("F:\\test.txt", "rw").getChannel();  
        //内存映射可读写文件，并映射至整个文件  
        MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, LENGTH);  
        for (int i = 0; i < LENGTH; i++) {//写满2M内容  
            out.put((byte) 'x');  
        }  
        //锁定前1/3内容  
        new LockAndModify(out, 0, 0 + LENGTH / 3);  
        //从文件中间开始锁定1/4内容，注，要锁定的内容一定不能有与  
        //已经锁定的内容，否则抛OverlappingFileLockException  
        new LockAndModify(out, LENGTH / 2, LENGTH / 2 + LENGTH / 4);  
    }  
  
    private static class LockAndModify extends Thread {  
        private ByteBuffer buff;  
        private int start, end;  
  
        LockAndModify(ByteBuffer mbb, int start, int end) {  
            this.start = start;  
            this.end = end;  
  
            //调整可最大读写位置  
            mbb.limit(end);  
            //调整读写起始位置  
            mbb.position(start);  
            //创建新的子缓冲区，但与原缓冲是共享同一片数据，  
            //只是缓冲区位置、界限和标记值是相互独立的  
            buff = mbb.slice();  
            start();  
        }  
  
        public void run() {  
            try {  
                // 获取独占锁，如果要锁定的部分被其他应用程序锁定，则会阻塞，至到获取锁为止  
                FileLock fl = fc.lock(start, end, false);  
                System.out.println("Locked: " + start + " to " + end);  
                System.out.println(buff.position() + "  " + buff.limit());  
  
                // 进行修改操作，前当前位置类  
                while (buff.position() < buff.limit() - 1) {  
                    buff.put((byte) (buff.get() + 1));  
                }  
                //JVM退出，或者channel关闭的时候会自动释放这些锁，但是你也可以用FileLock  
                //的release( )方法，明确地释放锁，就像这里释放锁一样  
                fl.release();  
                System.out.println("Released: " + start + " to " + end);  
            } catch (IOException e) {  
                throw new RuntimeException(e);  
            }  
        }  
    } 
}
