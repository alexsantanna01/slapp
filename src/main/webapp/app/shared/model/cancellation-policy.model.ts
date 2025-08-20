export interface ICancellationPolicy {
  id?: number;
  name?: string;
  description?: string | null;
  hoursBeforeEvent?: number;
  refundPercentage?: number;
  active?: boolean;
}

export const defaultValue: Readonly<ICancellationPolicy> = {
  active: false,
};
