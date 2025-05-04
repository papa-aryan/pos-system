package pos.integration;

import pos.model.Amount;

/*
 * Represents discount information retrieved from the DiscountDatabase.
 * Contains immutable information about an applicable discount.
 */
public final class DiscountInfoDTO {
    private final Amount discountAmount; 
    private final int discountPercentage;
    private final String discountType; 

    /*
     * Creates a new instance representing discount details.
     *
     * @param discountAmount The fixed amount of the discount (if applicable).
     * @param discountPercentage The percentage of the discount (if applicable).
     * @param discountType A string indicating the type of discount.
     */
    public DiscountInfoDTO(Amount discountAmount, int discountPercentage, String discountType) {
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
        this.discountType = discountType;
    }

    // Getters

    public Amount getDiscountAmount() {
        return discountAmount;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public String getDiscountType() {
        return discountType;
    }
}