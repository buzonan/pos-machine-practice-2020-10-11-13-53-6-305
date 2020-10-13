package pos.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {

        List<String> receiptItems = new ArrayList<>();;
        List<ItemInfo> itemInfoList = ItemDataLoader.loadAllItemInfos();
        List<String> filterBarcodes = barcodes.stream().distinct().collect(Collectors.toList());
        int total = 0;
        for(int x = 0; x < filterBarcodes.size(); x++){
            ItemDetails itemDetails = getBarcodeData(filterBarcodes.get(x), itemInfoList);
            int itemQuantity = getQuantity(itemDetails, barcodes);

            itemDetails.setQuantity(itemQuantity);
            itemDetails.setSubtotal(computeSubtotalPrice(itemDetails, itemQuantity));

            receiptItems.add(assembleReceiptLine(itemDetails));
            total += itemDetails.getSubtotal();
        }
        return assembleReceipt(receiptItems, total);
    }

    private String assembleReceipt(List<String> receiptItems, int total) {
        String strLine = "***<store earning no money>Receipt***\n";
        for(String item: receiptItems){
            strLine += item;
        }
        strLine += "----------------------\n" +
                "Total: "+total+" (yuan)\n" +
                "**********************";
        return strLine;
    }

    private String assembleReceiptLine(ItemDetails barcodeDetail) {
        return "Name: "+barcodeDetail.getName()+", Quantity: "+barcodeDetail.getQuantity()+", Unit price: "+barcodeDetail.getPrice()+" (yuan), Subtotal: "+barcodeDetail.getSubtotal()+" (yuan)\n";
    }

    private int computeSubtotalPrice(ItemDetails barcodeDetails, int itemQuantity) {
        int subtotal = 0;
        subtotal = barcodeDetails.getPrice() * itemQuantity;
        return subtotal;
    }

    private int getQuantity(ItemDetails barcode, List<String> barcodes) {
        int quantity = 0;
        for(int x = 0; x < barcodes.size(); x++){
            if(barcodes.get(x).equals(barcode.getBarcode())){
                quantity++;
            }
        }
        return quantity;
    }

    private ItemDetails getBarcodeData(String barcode, List<ItemInfo> barcodeInfos) {
        for(int x = 0; x < barcodeInfos.size(); x++){
            if(barcode.equals(barcodeInfos.get(x).getBarcode())){
                ItemDetails barcodeDetails = new ItemDetails();
                barcodeDetails.setBarcode(barcodeInfos.get(x).getBarcode());
                barcodeDetails.setName(barcodeInfos.get(x).getName());
                barcodeDetails.setPrice(barcodeInfos.get(x).getPrice());
                return barcodeDetails;
            }
        }
        return null;
    }


}
