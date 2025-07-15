
export interface Word {
  word: string;
  matches: number;
}

export interface ApiResponse {
  success: boolean;
  message?: string;
  data?: Word[];
}
