package cl.proteinlab.rfid.listeners;

import com.impinj.octane.BitPointers;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.MemoryBank;
import com.impinj.octane.PcBits;
import com.impinj.octane.SequenceState;
import com.impinj.octane.Tag;
import com.impinj.octane.TagData;
import com.impinj.octane.TagOp;
import com.impinj.octane.TagOpCompleteListener;
import com.impinj.octane.TagOpReport;
import com.impinj.octane.TagOpResult;
import com.impinj.octane.TagOpSequence;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;
import com.impinj.octane.TagWriteOp;
import com.impinj.octane.TagWriteOpResult;
import com.impinj.octane.TargetTag;
import com.impinj.octane.WordPointers;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Patricio A. Pérez Valverde
 * @since 23-03-17
 */
@Component
public class TagWriterReportListener implements TagReportListener, TagOpCompleteListener {

    private static short EPC_OP_ID = 123;
    private static short PC_BITS_OP_ID = 321;
    private static int opSpecID = 1;
    private static int outstanding = 0;
    private static Random r = new Random();

    private static String getRandomEpc() {
        String epc = "";

        // get the length of the EPC from 1 to 8 words
        int numwords = r.nextInt((6 - 1) + 1) + 1;
        // int numwords = 1;

        for (int i = 0; i < numwords; i++) {
            Short s = (short) r.nextInt(Short.MAX_VALUE + 1);
            epc += String.format("%04X", s);
        }
        return epc;
    }

    @Override
    public void onTagOpComplete(ImpinjReader reader, TagOpReport results) {
        System.out.println("TagOpComplete: ");
        for (TagOpResult t : results.getResults()) {
            System.out.print("  EPC: " + t.getTag().getEpc().toHexString());
            if (t instanceof TagWriteOpResult) {
                TagWriteOpResult tr = (TagWriteOpResult) t;

                if (tr.getOpId() == EPC_OP_ID) {
                    System.out.print("  Write to EPC Complete: ");
                } else if (tr.getOpId() == PC_BITS_OP_ID) {
                    System.out.print("  Write to PC Complete: ");
                }
                System.out.println(" result: " + tr.getResult().toString()
                        + " words_written: " + tr.getNumWordsWritten());
                outstanding--;
            }
        }
    }

    @Override
    public void onTagReported(ImpinjReader reader, TagReport report) {
        List<Tag> tags = report.getTags();

        for (Tag t : tags) {
            String newEpc = getRandomEpc();

            if (t.isPcBitsPresent()) {
                short pc = t.getPcBits();
                String currentEpc = t.getEpc().toHexString();

                try {
                    programEpc(currentEpc, pc, newEpc, reader);
                } catch (Exception e) {
                    System.out.println("Failed To program EPC: " + e.toString());
                }
            }
        }
    }

    private void programEpc(String currentEpc, short currentPC, String newEpc, ImpinjReader reader)
            throws Exception {
        if ((currentEpc.length() % 4 != 0) || (newEpc.length() % 4 != 0)) {
            throw new Exception("EPCs must be a multiple of 16- bits: "
                    + currentEpc + "  " + newEpc);
        }

        if (outstanding > 0) {
            return;
        }

        System.out.println("Programming Tag ");
        System.out.println("   EPC " + currentEpc + " to " + newEpc);

        TagOpSequence seq = new TagOpSequence();
        seq.setOps(new ArrayList<TagOp>());
        seq.setExecutionCount((short) 1); // delete after one time
        seq.setState(SequenceState.Active);
        seq.setId(opSpecID++);

        seq.setTargetTag(new TargetTag());
        seq.getTargetTag().setBitPointer(BitPointers.Epc);
        seq.getTargetTag().setMemoryBank(MemoryBank.Epc);
        seq.getTargetTag().setData(currentEpc);

        TagWriteOp epcWrite = new TagWriteOp();
        epcWrite.Id = EPC_OP_ID;
        epcWrite.setMemoryBank(MemoryBank.Epc);
        epcWrite.setWordPointer(WordPointers.Epc);
        epcWrite.setData(TagData.fromHexString(newEpc));

        // add to the list
        seq.getOps().add(epcWrite);

        // have to program the PC bits if these are not the same
        if (currentEpc.length() != newEpc.length()) {
            // keep other PC bits the same.
            String currentPCString = PcBits.toHexString(currentPC);

            short newPC = PcBits.AdjustPcBits(currentPC,
                    (short) (newEpc.length() / 4));
            String newPCString = PcBits.toHexString(newPC);

            System.out.println("   PC bits to establish new length: "
                    + newPCString + " " + currentPCString);

            TagWriteOp pcWrite = new TagWriteOp();
            pcWrite.Id = PC_BITS_OP_ID;
            pcWrite.setMemoryBank(MemoryBank.Epc);
            pcWrite.setWordPointer(WordPointers.PcBits);

            pcWrite.setData(TagData.fromHexString(newPCString));
            seq.getOps().add(pcWrite);
        }

        outstanding++;
        reader.addOpSequence(seq);
    }
}
